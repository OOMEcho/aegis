<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>邮箱验证码</title>
    <style>
      * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
      }

      body {
        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
        line-height: 1.6;
        color: #333;
        background-color: #f5f5f5;
      }

      .email-container {
        max-width: 600px;
        margin: 0 auto;
        background-color: #ffffff;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        border-radius: 8px;
        overflow: hidden;
      }

      .header {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
        text-align: center;
        padding: 40px 20px;
      }

      .header h1 {
        font-size: 28px;
        font-weight: 600;
        margin-bottom: 8px;
      }

      .header p {
        font-size: 16px;
        opacity: 0.9;
      }

      .content {
        padding: 40px 30px;
        text-align: center;
      }

      .greeting {
        font-size: 18px;
        color: #2c3e50;
        margin-bottom: 30px;
      }

      .verification-code {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
        font-size: 32px;
        font-weight: 700;
        padding: 20px 40px;
        border-radius: 10px;
        display: inline-block;
        margin: 30px 0;
        letter-spacing: 8px;
        box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
        transition: transform 0.2s ease;
      }

      .verification-code:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
      }

      .description {
        font-size: 16px;
        color: #555;
        margin-bottom: 25px;
        line-height: 1.7;
      }

      .expire-info {
        background-color: #fff3cd;
        border: 1px solid #ffeaa7;
        border-radius: 6px;
        padding: 15px;
        margin: 25px 0;
        font-size: 14px;
        color: #856404;
      }

      .security-tips {
        background-color: #f8f9fa;
        border-left: 4px solid #667eea;
        padding: 20px;
        margin: 30px 0;
        text-align: left;
      }

      .security-tips h3 {
        color: #2c3e50;
        font-size: 16px;
        margin-bottom: 10px;
      }

      .security-tips ul {
        color: #666;
        font-size: 14px;
        list-style-position: inside;
      }

      .security-tips li {
        margin-bottom: 5px;
      }

      .footer {
        background-color: #f8f9fa;
        padding: 20px 30px;
        text-align: center;
        border-top: 1px solid #e9ecef;
      }

      .footer p {
        font-size: 14px;
        color: #666;
        margin-bottom: 5px;
      }

      .contact-info {
        margin-top: 15px;
      }

      .contact-info a {
        color: #667eea;
        text-decoration: none;
      }

      .contact-info a:hover {
        text-decoration: underline;
      }

      @media (max-width: 480px) {
        .email-container {
          margin: 0;
          border-radius: 0;
        }

        .content {
          padding: 30px 20px;
        }

        .verification-code {
          font-size: 28px;
          letter-spacing: 6px;
          padding: 15px 30px;
        }

        .header h1 {
          font-size: 24px;
        }
      }
    </style>
</head>
<body>
<div class="email-container">
    <div class="header">
        <p>请使用下方验证码完成验证</p>
    </div>

    <div class="content">
        <div class="greeting">
            您好！${email}
        </div>

        <p class="description">
            您正在进行邮箱验证，请使用以下验证码完成验证：
        </p>

        <div class="verification-code">
            ${code}
        </div>

        <div class="expire-info">
            ⏰ 验证码将在 <strong>${expireMinutes} 分钟</strong> 后过期，请尽快完成验证。
        </div>

        <div class="security-tips">
            <h3>🛡️ 安全提醒</h3>
            <ul>
                <li>请勿将验证码告诉他人</li>
                <li>如果这不是您本人的操作，请忽略此邮件</li>
                <li>验证码仅用于本次验证，过期后将失效</li>
            </ul>
        </div>
    </div>

    <div class="footer">
        <p>此邮件由系统自动发送，请勿回复。</p>
        <div class="contact-info">
            <p>如有疑问，请联系客服：<a href="mailto:support@example.com">aegis_system@163.com</a></p>
        </div>
    </div>
</div>
</body>
</html>
