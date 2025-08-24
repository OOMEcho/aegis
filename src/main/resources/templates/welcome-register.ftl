<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>欢迎加入${websiteName}管理系统！</title>
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
        background-color: #f8fafc;
      }

      .email-container {
        max-width: 680px;
        margin: 20px auto;
        background-color: #ffffff;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
        border-radius: 16px;
        overflow: hidden;
      }

      .header {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
        text-align: center;
        padding: 50px 30px;
        position: relative;
      }

      .header::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 20"><defs><radialGradient id="g" cx="50%" cy="0%" r="50%"><stop offset="0%" stop-color="%23ffffff" stop-opacity="0.1"/><stop offset="100%" stop-color="%23ffffff" stop-opacity="0"/></radialGradient></defs><rect width="100" height="20" fill="url(%23g)"/></svg>') repeat-x;
      }

      .header-content {
        position: relative;
        z-index: 1;
      }

      .system-icon {
        font-size: 56px;
        margin-bottom: 20px;
        display: block;
      }

      .header h1 {
        font-size: 32px;
        font-weight: 700;
        margin-bottom: 12px;
        text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
      }

      .header p {
        font-size: 18px;
        opacity: 0.9;
        font-weight: 300;
      }

      .content {
        padding: 50px 40px;
      }

      .greeting {
        font-size: 24px;
        color: #1f2937;
        margin-bottom: 25px;
        text-align: center;
      }

      .username {
        color: #667eea;
        font-weight: 700;
      }

      .welcome-message {
        font-size: 16px;
        color: #4b5563;
        margin-bottom: 35px;
        text-align: center;
        line-height: 1.8;
        background-color: #f0f9ff;
        padding: 20px;
        border-radius: 10px;
        border-left: 4px solid #3b82f6;
      }

      .system-features {
        margin: 40px 0;
      }

      .features-title {
        font-size: 20px;
        color: #1f2937;
        font-weight: 600;
        text-align: center;
        margin-bottom: 30px;
      }

      .features-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
        gap: 25px;
      }

      .feature-card {
        background: linear-gradient(135deg, #f8faff 0%, #f1f5ff 100%);
        padding: 25px;
        border-radius: 12px;
        border: 1px solid #e5e7eb;
        transition: all 0.3s ease;
      }

      .feature-card:hover {
        transform: translateY(-3px);
        box-shadow: 0 8px 25px rgba(59, 130, 246, 0.15);
      }

      .feature-header {
        display: flex;
        align-items: center;
        margin-bottom: 15px;
      }

      .feature-icon {
        font-size: 24px;
        margin-right: 12px;
      }

      .feature-title {
        font-size: 18px;
        font-weight: 600;
        color: #1f2937;
      }

      .feature-description {
        font-size: 14px;
        color: #6b7280;
        line-height: 1.6;
      }

      .permission-notice {
        background: linear-gradient(135deg, #fef3c7 0%, #fde68a 100%);
        border: 1px solid #f59e0b;
        border-radius: 10px;
        padding: 25px;
        margin: 30px 0;
        text-align: center;
      }

      .notice-icon {
        font-size: 24px;
        margin-bottom: 10px;
        display: block;
      }

      .notice-title {
        font-size: 18px;
        font-weight: 600;
        color: #92400e;
        margin-bottom: 10px;
      }

      .notice-text {
        font-size: 14px;
        color: #78350f;
        line-height: 1.6;
      }

      .security-tips {
        background-color: #fef2f2;
        border-left: 4px solid #ef4444;
        padding: 25px;
        margin: 30px 0;
        border-radius: 0 8px 8px 0;
      }

      .tips-title {
        color: #dc2626;
        font-size: 18px;
        font-weight: 600;
        margin-bottom: 15px;
        display: flex;
        align-items: center;
      }

      .tips-title::before {
        content: '🔐';
        margin-right: 10px;
        font-size: 20px;
      }

      .tips-list {
        color: #991b1b;
        font-size: 14px;
      }

      .tips-list li {
        margin-bottom: 8px;
        line-height: 1.6;
      }

      .footer {
        background-color: #1f2937;
        color: white;
        text-align: center;
        padding: 30px;
      }

      .footer-text {
        font-size: 14px;
        opacity: 0.8;
        margin-bottom: 15px;
      }

      .copyright {
        font-size: 12px;
        opacity: 0.6;
      }

      @media (max-width: 640px) {
        .email-container {
          margin: 10px;
          border-radius: 12px;
        }

        .header {
          padding: 40px 20px;
        }

        .header h1 {
          font-size: 26px;
        }

        .content {
          padding: 30px 20px;
        }

        .features-grid {
          grid-template-columns: 1fr;
          gap: 20px;
        }
      }
    </style>
</head>
<body>
<div class="email-container">
    <div class="header">
        <div class="header-content">
            <span class="system-icon">⚡</span>
            <h1>欢迎加入 ${websiteName}</h1>
            <p>基于 Spring Boot + Vue 的 RBAC 权限管理系统</p>
        </div>
    </div>

    <div class="content">
        <div class="greeting">
            你好，<span class="username">${username}</span>！
        </div>

        <div class="welcome-message">
            🎉 恭喜您成功注册 <strong>${websiteName}</strong> 管理系统！<br>
            您的账户已经创建成功，现在可以开始使用我们的权限管理系统了。
        </div>

        <div class="system-features">
            <div class="features-title">🔧 系统核心功能</div>
            <div class="features-grid">
                <div class="feature-card">
                    <div class="feature-header">
                        <span class="feature-icon">👥</span>
                        <div class="feature-title">用户管理</div>
                    </div>
                    <div class="feature-description">
                        完整的用户生命周期管理，支持用户创建、编辑、禁用等操作，实现精细化用户控制
                    </div>
                </div>

                <div class="feature-card">
                    <div class="feature-header">
                        <span class="feature-icon">🛡️</span>
                        <div class="feature-title">角色权限</div>
                    </div>
                    <div class="feature-description">
                        灵活的RBAC权限模型，支持角色定义、权限分配，实现基于角色的访问控制
                    </div>
                </div>

                <div class="feature-card">
                    <div class="feature-header">
                        <span class="feature-icon">📊</span>
                        <div class="feature-title">数据统计</div>
                    </div>
                    <div class="feature-description">
                        实时数据报表和统计分析，帮助管理员了解系统使用情况和用户行为
                    </div>
                </div>

                <div class="feature-card">
                    <div class="feature-header">
                        <span class="feature-icon">📝</span>
                        <div class="feature-title">操作日志</div>
                    </div>
                    <div class="feature-description">
                        完整的操作审计日志，记录所有用户操作行为，确保系统安全可追溯
                    </div>
                </div>
            </div>
        </div>

        <div class="permission-notice">
            <span class="notice-icon">⚠️</span>
            <div class="notice-title">权限提醒</div>
            <div class="notice-text">
                您当前为新注册用户，具有基础访问权限。如需更多权限，请联系系统管理员进行角色分配。
            </div>
        </div>

        <div class="security-tips">
            <div class="tips-title">安全须知</div>
            <ul class="tips-list">
                <li>请妥善保管您的登录凭据，不要与他人共享账户信息</li>
                <li>建议定期修改密码，密码应包含字母、数字和特殊字符</li>
                <li>如发现账户异常活动，请立即联系系统管理员</li>
                <li>退出系统时请确保完全注销，特别是在公共设备上</li>
                <li>系统会记录您的所有操作日志，请规范使用系统功能</li>
            </ul>
        </div>
    </div>

    <div class="footer">
        <div class="footer-text">
            此邮件由 ${websiteName} 系统自动发送，请勿回复。
        </div>
        <div class="copyright">
            © ${year} ${websiteName}. 基于 Spring Boot + Vue 构建
        </div>
    </div>
</div>
</body>
</html>
