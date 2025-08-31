package com.aegis.common.captcha;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.aegis.common.exception.BusinessException;
import com.aegis.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * @Author: xuesong.lei
 * @Date: 2025/8/31 20:11
 * @Description: 验证码实现类
 */
@Service
@RequiredArgsConstructor
public class SlideCaptchaService {

    private final RedisUtils redisUtils;

    private static final int CAPTCHA_WIDTH = 320;
    private static final int CAPTCHA_HEIGHT = 180;
    private static final int SLIDER_WIDTH = 60;
    private static final int SLIDER_HEIGHT = 60;
    private static final int TOLERANCE = 5; // 容错范围

    /**
     * 生成滑动验证码
     */
    public CaptchaVO generateCaptcha() {
        try {
            String captchaKey = IdUtil.fastSimpleUUID();

            // 随机生成滑块位置
            int sliderX = RandomUtil.randomInt(SLIDER_WIDTH, CAPTCHA_WIDTH - SLIDER_WIDTH);
            int sliderY = RandomUtil.randomInt(10, CAPTCHA_HEIGHT - SLIDER_HEIGHT - 10);

            // 加载随机背景图片
            BufferedImage backgroundImage = loadRandomBackgroundImage();

            // 生成随机滑块形状
            Shape sliderShape = generateRandomSliderShape();

            // 先提取滑块内容（在挖洞之前）
            BufferedImage sliderImage = extractSliderContent(backgroundImage, sliderX, sliderY, sliderShape);

            // 然后在背景图上挖出滑块形状的洞
            cutSliderShape(backgroundImage, sliderX, sliderY, sliderShape);

            // 将图片转换为Base64
            String backgroundBase64 = imageToBase64(backgroundImage);
            String sliderBase64 = imageToBase64(sliderImage);

            // 将正确答案存储到Redis，过期时间5分钟
            redisUtils.set("captcha:" + captchaKey,
                    String.valueOf(sliderX), 5, TimeUnit.MINUTES);

            return new CaptchaVO(captchaKey, backgroundBase64, sliderBase64, sliderY);

        } catch (Exception e) {
            throw new BusinessException("生成验证码失败");
        }
    }

    /**
     * 调整图片尺寸
     */
    private BufferedImage resizeImage(BufferedImage originalImage) {
        BufferedImage resizedImage = new BufferedImage(CAPTCHA_WIDTH, CAPTCHA_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();

        // 设置高质量缩放
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 计算缩放比例，保持宽高比
        double scaleX = (double) CAPTCHA_WIDTH / originalImage.getWidth();
        double scaleY = (double) CAPTCHA_HEIGHT / originalImage.getHeight();
        double scale = Math.max(scaleX, scaleY); // 使用较大的比例确保填满

        int scaledWidth = (int) (originalImage.getWidth() * scale);
        int scaledHeight = (int) (originalImage.getHeight() * scale);

        // 居中绘制
        int x = (CAPTCHA_WIDTH - scaledWidth) / 2;
        int y = (CAPTCHA_HEIGHT - scaledHeight) / 2;

        g2d.drawImage(originalImage, x, y, scaledWidth, scaledHeight, null);
        g2d.dispose();
        return resizedImage;
    }

    /**
     * 生成随机滑块形状
     */
    private Shape generateRandomSliderShape() {
        int shapeType = RandomUtil.randomInt(4);

        switch (shapeType) {
            case 0:
                return createPuzzleShape(); // 拼图形状
            case 1:
                return createStarShape(); // 星形
            case 2:
                return createHexagonShape(); // 六边形
            case 3:
                return createIrregularShape(); // 不规则形状
            default:
                return createPuzzleShape();
        }
    }

    /**
     * 创建拼图形状
     */
    private Shape createPuzzleShape() {
        int centerX = SLIDER_WIDTH / 2;
        int centerY = SLIDER_HEIGHT / 2;
        int radius = 25;

        Path2D path = new Path2D.Double();

        // 基础圆形
        path.append(new Ellipse2D.Double(centerX - radius, centerY - radius, radius * 2, radius * 2), false);

        // 添加拼图凸起（随机位置）
        int bulgeSide = RandomUtil.randomInt(4); // 0上 1右 2下 3左
        int bulgeSize = 8;

        switch (bulgeSide) {
            case 0: // 上
                path.append(new Ellipse2D.Double(centerX - bulgeSize, centerY - radius - bulgeSize,
                        bulgeSize * 2, bulgeSize * 2), false);
                break;
            case 1: // 右
                path.append(new Ellipse2D.Double(centerX + radius - bulgeSize, centerY - bulgeSize,
                        bulgeSize * 2, bulgeSize * 2), false);
                break;
            case 2: // 下
                path.append(new Ellipse2D.Double(centerX - bulgeSize, centerY + radius - bulgeSize,
                        bulgeSize * 2, bulgeSize * 2), false);
                break;
            case 3: // 左
                path.append(new Ellipse2D.Double(centerX - radius - bulgeSize, centerY - bulgeSize,
                        bulgeSize * 2, bulgeSize * 2), false);
                break;
        }

        return path;
    }

    /**
     * 创建星形
     */
    private Shape createStarShape() {
        int centerX = SLIDER_WIDTH / 2;
        int centerY = SLIDER_HEIGHT / 2;
        int outerRadius = 25;
        int innerRadius = 12;
        int points = 5;

        Path2D path = new Path2D.Double();

        for (int i = 0; i < points * 2; i++) {
            double angle = Math.PI * i / points;
            int radius = (i % 2 == 0) ? outerRadius : innerRadius;
            double x = centerX + radius * Math.cos(angle - Math.PI / 2);
            double y = centerY + radius * Math.sin(angle - Math.PI / 2);

            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.closePath();

        return path;
    }

    /**
     * 创建六边形
     */
    private Shape createHexagonShape() {
        int centerX = SLIDER_WIDTH / 2;
        int centerY = SLIDER_HEIGHT / 2;
        int radius = 25;

        Path2D path = new Path2D.Double();

        for (int i = 0; i < 6; i++) {
            double angle = Math.PI * i / 3;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);

            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.closePath();

        return path;
    }

    /**
     * 创建不规则形状
     */
    private Shape createIrregularShape() {
        int centerX = SLIDER_WIDTH / 2;
        int centerY = SLIDER_HEIGHT / 2;

        Path2D path = new Path2D.Double();

        // 生成不规则的多边形
        int vertices = RandomUtil.randomInt(6, 10);
        double baseRadius = 20;

        for (int i = 0; i < vertices; i++) {
            double angle = 2 * Math.PI * i / vertices;
            // 随机变化半径，创造不规则效果
            double radiusVariation = RandomUtil.randomDouble(0.7, 1.3);
            double radius = baseRadius * radiusVariation;

            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);

            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.closePath();

        return path;
    }

    /**
     * 加载随机背景图片
     */
    private BufferedImage loadRandomBackgroundImage() {
        try {
            // 随机选择1-5中的一个图片
            int imageIndex = RandomUtil.randomInt(1, 6);
            String imagePath = "/static/captcha/bg" + imageIndex + ".jpg";

            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream == null) {
                throw new RuntimeException("背景图片不存在: " + imagePath);
            }

            BufferedImage originalImage = ImageIO.read(imageStream);
            imageStream.close();

            if (originalImage == null) {
                throw new RuntimeException("无法读取背景图片: " + imagePath);
            }

            // 调整图片大小到验证码尺寸
            return resizeImage(originalImage);

        } catch (Exception e) {
            throw new RuntimeException("加载背景图片失败: " + e.getMessage(), e);
        }
    }

    /**
     * 从背景图提取滑块内容
     */
    private BufferedImage extractSliderContent(BufferedImage backgroundImage, int x, int y, Shape shape) {
        // 创建滑块图像，使用透明背景
        BufferedImage sliderImage = new BufferedImage(SLIDER_WIDTH, SLIDER_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = sliderImage.createGraphics();

        // 设置高质量渲染
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // 先绘制背景区域的内容
        BufferedImage sliderArea = backgroundImage.getSubimage(x, y, SLIDER_WIDTH, SLIDER_HEIGHT);
        g2d.drawImage(sliderArea, 0, 0, null);

        // 使用形状作为剪裁路径，只保留形状内的内容
        g2d.setComposite(AlphaComposite.DstIn);
        g2d.setColor(Color.WHITE);
        g2d.fill(shape);

        // 添加白色边框
        g2d.setComposite(AlphaComposite.SrcOver);
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.draw(shape);

        // 添加内部阴影效果，增强立体感
        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.setStroke(new BasicStroke(1.0f));
        AffineTransform inset = AffineTransform.getTranslateInstance(1, 1);
        Shape insetShape = inset.createTransformedShape(shape);
        g2d.draw(insetShape);

        g2d.dispose();
        return sliderImage;
    }

    /**
     * 在背景图上挖出滑块形状的洞（修改后的方法）
     */
    private void cutSliderShape(BufferedImage backgroundImage, int x, int y, Shape shape) {
        Graphics2D g2d = backgroundImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 移动形状到指定位置
        AffineTransform transform = AffineTransform.getTranslateInstance(x, y);
        Shape transformedShape = transform.createTransformedShape(shape);

        // 创建一个稍微暗一点的颜色来填充洞
        // 获取洞周围的平均颜色
        Color holeColor = getAverageColor(backgroundImage, x, y, SLIDER_WIDTH, SLIDER_HEIGHT);
        Color darkerColor = new Color(
            Math.max(0, holeColor.getRed() - 30),
            Math.max(0, holeColor.getGreen() - 30),
            Math.max(0, holeColor.getBlue() - 30)
        );

        // 填充洞
        g2d.setColor(darkerColor);
        g2d.fill(transformedShape);

        // 添加洞的边框效果
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.setStroke(new BasicStroke(2.0f));
        g2d.draw(transformedShape);

        // 添加内阴影效果
        g2d.setColor(new Color(0, 0, 0, 60));
        g2d.setStroke(new BasicStroke(1.0f));
        AffineTransform inward = AffineTransform.getTranslateInstance(1, 1);
        Shape inwardShape = inward.createTransformedShape(transformedShape);
        g2d.draw(inwardShape);

        g2d.dispose();
    }

    /**
     * 获取指定区域的平均颜色
     */
    private Color getAverageColor(BufferedImage image, int x, int y, int width, int height) {
        long totalRed = 0, totalGreen = 0, totalBlue = 0;
        int pixelCount = 0;

        for (int i = x; i < x + width && i < image.getWidth(); i++) {
            for (int j = y; j < y + height && j < image.getHeight(); j++) {
                Color pixelColor = new Color(image.getRGB(i, j));
                totalRed += pixelColor.getRed();
                totalGreen += pixelColor.getGreen();
                totalBlue += pixelColor.getBlue();
                pixelCount++;
            }
        }

        if (pixelCount == 0) {
            return Color.GRAY;
        }

        return new Color(
            (int) (totalRed / pixelCount),
            (int) (totalGreen / pixelCount),
            (int) (totalBlue / pixelCount)
        );
    }

    /**
     * 将图片转换为Base64字符串
     */
    private String imageToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        byte[] imageBytes = baos.toByteArray();
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(imageBytes);
    }

    /**
     * 验证滑动验证码
     */
    public boolean verifyCaptcha(String captchaKey, int userX) {
        String correctXStr = redisUtils.get("captcha:" + captchaKey);
        if (correctXStr == null) {
            return false; // 验证码已过期
        }

        int correctX = Integer.parseInt(correctXStr);
        boolean isValid = Math.abs(userX - correctX) <= TOLERANCE;

        // 验证后删除，防止重复使用
        redisUtils.delete("captcha:" + captchaKey);

        return isValid;
    }
}
