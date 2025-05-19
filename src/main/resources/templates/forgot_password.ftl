<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Forgot Password</title>
    <style>
        .button {
            display: inline-block;
            padding: 10px 20px;
            margin-top: 20px;
            font-size: 16px;
            color: white;
            background-color: #007bff;
            text-decoration: none;
            border-radius: 5px;
        }

        .button:hover {
            background-color: #0056b3;
        }

        .container {
            font-family: Arial, sans-serif;
            padding: 20px;
        }

        .footer {
            margin-top: 30px;
            font-size: 12px;
            color: #666;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Hello, ${name}!</h2>
    <p>We received a request to reset your password. Click the button below to proceed:</p>
    <a class="button" href="https://aitunet.kz/auth/recover?email=${email}&token=${token}">Reset Password</a>
    <p>If you didn't request a password reset, you can ignore this email.</p>
    <div class="footer">
        <p>This message was sent by AITU Net. Do not reply to this email.</p>
    </div>
</div>
</body>
</html>
