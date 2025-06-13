# 🧪 SCRIPT DE PRUEBAS PARA EMAILS REALES

Write-Host "🚀 INICIANDO PRUEBAS DE EMAIL EN MODO PRODUCCIÓN" -ForegroundColor Green
Write-Host "================================================" -ForegroundColor Cyan

# Esperar a que la aplicación esté lista
Write-Host "⏳ Esperando a que la aplicación esté lista..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

# Test 1: Verificar conectividad SMTP
Write-Host "`n🔍 TEST 1: Verificando conectividad SMTP..." -ForegroundColor Blue
try {
    $response1 = Invoke-RestMethod -Uri "http://localhost:8080/astrodev/contacto/test-smtp" -Method GET
    Write-Host "✅ Conectividad SMTP: $($response1.message)" -ForegroundColor Green
    Write-Host "📊 Modo: $($response1.modo)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Error en test SMTP: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Enviar email de prueba
Write-Host "`n📧 TEST 2: Enviando email de prueba..." -ForegroundColor Blue
try {
    $response2 = Invoke-RestMethod -Uri "http://localhost:8080/astrodev/contacto/test-email" -Method POST
    Write-Host "✅ Email de prueba: $($response2.message)" -ForegroundColor Green
} catch {
    Write-Host "❌ Error en envío de prueba: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Enviar formulario de contacto completo
Write-Host "`n📝 TEST 3: Enviando formulario de contacto..." -ForegroundColor Blue

$contactoData = @{
    nombre = "Usuario de Prueba"
    email = "prueba@ejemplo.com"
    telefono = "+51 987 654 321"
    motivo = "INFORMACION_CURSOS"
    mensaje = "Este es un mensaje de prueba enviado desde el sistema de AstroDev Academy para verificar que el envío de emails esté funcionando correctamente."
} | ConvertTo-Json -Depth 10

try {
    $response3 = Invoke-RestMethod -Uri "http://localhost:8080/astrodev/contacto/enviar" -Method POST -Body $contactoData -ContentType "application/json; charset=utf-8"
    Write-Host "✅ Formulario enviado: $($response3.message)" -ForegroundColor Green
    Write-Host "📊 Modo: $($response3.modo)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Error en formulario: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n🎉 PRUEBAS COMPLETADAS" -ForegroundColor Green
Write-Host "================================================" -ForegroundColor Cyan
Write-Host "📬 Revisa tu email guiro240303@gmail.com para ver los mensajes recibidos." -ForegroundColor Yellow
