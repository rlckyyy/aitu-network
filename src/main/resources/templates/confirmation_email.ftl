<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        .btn {
            background-color: #28a745;
            color: white;
            padding: 12px 24px;
            text-decoration: none;
            border-radius: 5px;
            font-weight: bold;
        }

        .container {
            font-family: Arial, sans-serif;
            padding: 20px;
            background-color: #f9f9f9;
            border-radius: 8px;
            width: 90%;
            max-width: 600px;
            margin: auto;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Hello, ${name}!</h2>
    <p>Thank you for registering. Please confirm your email address by clicking the button below:</p>
    <p>
        <a class="btn" href="${confirmationUrl}">Confirm Email</a>
    </p>
    <p>If the button doesn't work, copy and paste this link into your browser:</p>
    <p><a href="${confirmationUrl}">${confirmationUrl}</a></p>
    <p>This link will expire in 24 hours.</p>
    <br>
    <p>Best regards,<br>The Team</p>
</div>
</body>
</html>
