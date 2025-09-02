package com.aegis.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.aegis.common.captcha.CaptchaVO;
import com.aegis.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/31 20:11
 * @Description: 滑块验证码工具类
 */
@Slf4j
@Component
@RequiredArgsConstructor
public final class CaptchaUtils {

    private final RedisUtils redisUtils;

    // 验证码在Redis中的key前缀
    public static final String SLIDER_CAPTCHA_KEY = "captcha:";

    // 画布宽高
    private static final int CANVAS_WIDTH = 320;
    private static final int CANVAS_HEIGHT = 180;

    // 阻塞块宽高/半径
    private static final int BLOCK_WIDTH = 60;
    private static final int BLOCK_HEIGHT = 60;
    public static final int BLOCK_RADIUS = 9;

    // 误差容忍度
    private static final int TOLERANCE = 5;

    /**
     * 生成滑动验证码
     */
    public CaptchaVO generateCaptcha() {
        try {
            // 生成唯一标识
            String captchaKey = IdUtil.fastSimpleUUID();

            // 随机生成滑块位置
            int blockX = RandomUtil.randomInt(BLOCK_WIDTH, CANVAS_WIDTH - BLOCK_WIDTH - 10);
            int blockY = RandomUtil.randomInt(10, CANVAS_HEIGHT - BLOCK_HEIGHT + 1);

            // 加载随机背景图片
            BufferedImage backgroundImage = loadRandomBackgroundImage();

            // 阻塞块
            BufferedImage sliderImage = new BufferedImage(BLOCK_WIDTH, BLOCK_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);

            // 新建的图像根据轮廓图颜色赋值，源图生成遮罩
            cutByTemplate(backgroundImage, sliderImage, blockX, blockY);

            // 将图片转换为Base64
            String backgroundBase64 = imageToBase64(backgroundImage);
            String sliderBase64 = imageToBase64(sliderImage);

            redisUtils.set(SLIDER_CAPTCHA_KEY + captchaKey, String.valueOf(blockX), 5, TimeUnit.MINUTES);

            return new CaptchaVO(captchaKey, backgroundBase64, sliderBase64, blockY);
        } catch (Exception e) {
            log.error("生成验证码失败: {}", e.getMessage());
            throw new BusinessException("生成验证码失败");
        }
    }

    /**
     * 验证验证码
     */
    public boolean verifyCaptcha(String captchaKey, Integer userX) {
        String correctXStr = redisUtils.get(SLIDER_CAPTCHA_KEY + captchaKey);
        if (correctXStr == null) {
            return false;
        }

        boolean isValid = Math.abs(userX - Integer.parseInt(correctXStr)) <= TOLERANCE;

        // 验证后删除，防止重复使用
        redisUtils.delete("captcha:" + captchaKey);

        return isValid;
    }

    /**
     * 获取验证码资源图
     **/
    private BufferedImage loadRandomBackgroundImage() {
        try {
            // 随机选择1-5中的一个图片
            int imageIndex = RandomUtil.randomInt(1, 6);
            String imagePath = "/static/captcha/bg" + imageIndex + ".jpg";

            InputStream imageStream = CaptchaUtils.class.getResourceAsStream(imagePath);
            if (imageStream == null) {
                log.error("背景图片不存在: {}", imagePath);
                throw new BusinessException("生成验证码失败");
            }

            BufferedImage originalImage = ImageIO.read(imageStream);
            imageStream.close();

            if (originalImage == null) {
                log.error("无法读取背景图片: {}", imagePath);
                throw new BusinessException("生成验证码失败");
            }

            // 调整图片大小到验证码尺寸
            return imageResize(originalImage);

        } catch (Exception e) {
            log.error("加载背景图片失败: {}", e.getMessage());
            throw new BusinessException("生成验证码失败");
        }
    }

    /**
     * 调整图片大小
     **/
    private BufferedImage imageResize(BufferedImage bufferedImage) {
        Image image = bufferedImage.getScaledInstance(CANVAS_WIDTH, CANVAS_HEIGHT, Image.SCALE_SMOOTH);
        BufferedImage resultImage = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resultImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, null);
        graphics2D.dispose();
        return resultImage;
    }

    /**
     * 抠图，并生成阻塞块
     **/
    private void cutByTemplate(BufferedImage canvasImage, BufferedImage blockImage, int blockX, int blockY) {
        BufferedImage waterImage = new BufferedImage(BLOCK_WIDTH, BLOCK_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
        // 阻塞块的轮廓图
        int[][] blockData = getBlockData();
        // 创建阻塞块具体形状
        for (int i = 0; i < BLOCK_WIDTH; i++) {
            for (int j = 0; j < BLOCK_HEIGHT; j++) {
                try {
                    // 原图中对应位置变色处理
                    if (blockData[i][j] == 1) {
                        // 背景设置为黑色
                        waterImage.setRGB(i, j, Color.BLACK.getRGB());
                        blockImage.setRGB(i, j, canvasImage.getRGB(blockX + i, blockY + j));
                        // 轮廓设置为白色，取带像素和无像素的界点，判断该点是不是临界轮廓点
                        if (blockData[i + 1][j] == 0 || blockData[i][j + 1] == 0 || blockData[i - 1][j] == 0 || blockData[i][j - 1] == 0) {
                            blockImage.setRGB(i, j, Color.WHITE.getRGB());
                            waterImage.setRGB(i, j, Color.WHITE.getRGB());
                        }
                    }
                    // 这里把背景设为透明
                    else {
                        blockImage.setRGB(i, j, Color.TRANSLUCENT);
                        waterImage.setRGB(i, j, Color.TRANSLUCENT);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    // 防止数组下标越界异常
                }
            }
        }
        // 在画布上添加阻塞块水印
        addBlockWatermark(canvasImage, waterImage, blockX, blockY);
    }

    /**
     * 构建拼图轮廓轨迹
     **/
    private int[][] getBlockData() {
        int[][] data = new int[BLOCK_WIDTH][BLOCK_HEIGHT];
        double po = Math.pow(BLOCK_RADIUS, 2);
        // 随机生成两个圆的坐标，在4个方向上 随机找到2个方向添加凸/凹
        // 凸/凹1
        int face1 = RandomUtils.nextInt(0, 4);
        // 凸/凹2
        int face2;
        // 保证两个凸/凹不在同一位置
        do {
            face2 = RandomUtils.nextInt(0, 4);
        } while (face1 == face2);
        // 获取凸/凹起位置坐标
        int[] circle1 = getCircleCoords(face1);
        int[] circle2 = getCircleCoords(face2);
        // 随机凸/凹类型
        int shape = RandomUtil.randomInt(0, 1);
        // 圆的标准方程 (x-a)²+(y-b)²=r²,标识圆心（a,b）,半径为r的圆
        // 计算需要的小图轮廓，用二维数组来表示，二维数组有两张值，0和1，其中0表示没有颜色，1有颜色
        for (int i = 0; i < BLOCK_WIDTH; i++) {
            for (int j = 0; j < BLOCK_HEIGHT; j++) {
                data[i][j] = 0;
                // 创建中间的方形区域
                if ((i >= BLOCK_RADIUS && i <= BLOCK_WIDTH - BLOCK_RADIUS && j >= BLOCK_RADIUS && j <= BLOCK_HEIGHT - BLOCK_RADIUS)) {
                    data[i][j] = 1;
                }
                double d1 = Math.pow(i - Objects.requireNonNull(circle1)[0], 2) + Math.pow(j - circle1[1], 2);
                double d2 = Math.pow(i - Objects.requireNonNull(circle2)[0], 2) + Math.pow(j - circle2[1], 2);
                // 创建两个凸/凹
                if (d1 <= po || d2 <= po) {
                    data[i][j] = shape;
                }
            }
        }
        return data;
    }

    /**
     * 根据朝向获取圆心坐标
     */
    private int[] getCircleCoords(int face) {
        // 上
        if (0 == face) {
            return new int[]{BLOCK_WIDTH / 2 - 1, BLOCK_RADIUS};
        }
        // 左
        else if (1 == face) {
            return new int[]{BLOCK_RADIUS, BLOCK_HEIGHT / 2 - 1};
        }
        // 下
        else if (2 == face) {
            return new int[]{BLOCK_WIDTH / 2 - 1, BLOCK_HEIGHT - BLOCK_RADIUS - 1};
        }
        // 右
        else if (3 == face) {
            return new int[]{BLOCK_WIDTH - BLOCK_RADIUS - 1, BLOCK_HEIGHT / 2 - 1};
        }
        return null;
    }

    /**
     * 在画布上添加阻塞块水印
     */
    private void addBlockWatermark(BufferedImage canvasImage, BufferedImage blockImage, int x, int y) {
        Graphics2D graphics2D = canvasImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.8f));
        graphics2D.drawImage(blockImage, x, y, null);
        graphics2D.dispose();
    }

    /**
     * 将图片转换为Base64字符串
     */
    private String imageToBase64(BufferedImage image) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", baos);
            byte[] imageBytes = baos.toByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            log.error("图片转换Base64失败: {}", e.getMessage());
            throw new BusinessException("生成验证码失败");
        }
    }
}
