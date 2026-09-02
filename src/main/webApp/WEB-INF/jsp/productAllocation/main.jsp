<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jspf/globalHeader.jspf" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Belex - Allocation des produits</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/general.css" type="text/css">
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: Arial, sans-serif; background-color: #f5f5f5; }
        .container { display: flex; height: 100vh; }

        .left-panel {
            width: 28%;
            background-color: #f9f9f9;
            border-right: 2px solid #ccc;
            overflow-y: auto;
            padding: 20px;
            box-shadow: inset -2px 0 5px rgba(0,0,0,0.05);
        }

        .right-panel {
            flex: 1;
            background-color: white;
            overflow-y: auto;
            padding: 20px;
        }

        .section { margin-bottom: 25px; }
        .section h3 { background-color: #000066; color: white; padding: 10px 15px; border-radius: 3px; margin-bottom: 15px; font-size: 14px; }
        .section p { margin: 10px 0; font-size: 13px; color: #666; }

        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; font-weight: bold; margin-bottom: 5px; font-size: 13px; }
        .form-group input, .form-group select { width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 3px; font-size: 13px; }
        .form-group input:focus, .form-group select:focus { outline: none; border-color: #000066; background-color: #f0f0ff; }

        .button-group { display: flex; gap: 10px; flex-direction: column; }
        .button { padding: 10px; background-color: #000066; color: white; border: none; border-radius: 3px; cursor: pointer; font-weight: bold; text-align: center; }
        .button:hover { background-color: #000099; }
        .button-secondary { background-color: #666; }
        .button-secondary:hover { background-color: #888; }

        @media (max-width: 1024px) {
            .left-panel { width: 35%; }
        }

        @media (max-width: 768px) {
            .container { flex-direction: column; height: auto; }
            .left-panel { width: 100%; border-right: none; border-bottom: 2px solid #ccc; }
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- LEFT PANEL: Main Menu -->
        <div class="left-panel">
            <div class="section">
                <h3>🔧 Menu d'allocation</h3>
                <p>Sélectionnez une option pour commencer :</p>
            </div>

            <div class="section button-group">
                <form action="${flowExecutionUrl}" method="post" style="margin: 0;">
                    <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
                    <button type="submit" name="_eventId_selectAllocationDateForm" class="button">📅 Sélectionner une date</button>
                </form>
                <form action="${flowExecutionUrl}" method="post" style="margin: 0;">
                    <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
                    <button type="submit" name="_eventId_selectSupplierForm" class="button">🏢 Sélectionner un fournisseur</button>
                </form>
            </div>
        </div>

        <!-- RIGHT PANEL: Welcome -->
        <div class="right-panel">
            <div class="section">
                <h3>📦 Attribution des produits</h3>
                <p style="margin-top: 20px;">
                    Bienvenue dans le module d'attribution des produits réceptionnés.<br><br>
                    Vous pouvez :<br>
                    • <strong>Sélectionner une date</strong> pour voir tous les fournisseurs de cette date<br>
                    • <strong>Sélectionner un fournisseur</strong> pour voir directement ses articles<br><br>
                    Cliquez sur un bouton à gauche pour commencer.
                </p>
            </div>
        </div>
    </div>
</body>
</html>
