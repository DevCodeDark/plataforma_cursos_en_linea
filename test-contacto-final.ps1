# Script de prueba para el sistema de emails de AstroDev Academy
# Autor: Sistema Automatizado
# Fecha: 12/06/2025

Write-Host "🚀 INICIANDO PRUEBAS DEL SISTEMA DE EMAIL - MODO PRODUCCION" -ForegroundColor Green
Write-Host "===========================================================" -ForegroundColor Cyan

# Test 1: Verificar conectividad SMTP
Write-Host "`n📡 Test 1: Verificando conectividad SMTP..." -ForegroundColor Yellow
try {
    $response1 = Invoke-RestMethod -Uri "http://localhost:8080/astrodev/contacto/test-smtp" -Method GET
    Write-Host "✅ SMTP Test: $($response1.message)" -ForegroundColor Green
    Write-Host "📊 Modo: $($response1.modo)" -ForegroundColor Cyan
    Write-Host "📋 Detalles: $($response1.detalles)" -ForegroundColor White
} catch {
    Write-Host "❌ Error en SMTP Test: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Test 2: Probar formulario de contacto
Write-Host "`n📧 Test 2: Enviando formulario de contacto..." -ForegroundColor Yellow

$contactoData = @{
    nombre = "Juan Carlos Mendoza"
    email = "juan.mendoza@ejemplo.com"
    telefono = "+51 987 654 321"
    motivo = "INFORMACION_CURSOS"
    mensaje = "Hola AstroDev Academy! Estoy muy interesado en conocer mas sobre los cursos de desarrollo espacial que ofrecen. Podrian enviarme informacion detallada sobre los programas disponibles, horarios, costos y requisitos? Tambien me gustaria saber sobre certificaciones. Gracias por su atencion. Saludos cordiales."
} | ConvertTo-Json -Depth 3

try {
    $response2 = Invoke-RestMethod -Uri "http://localhost:8080/astrodev/contacto/enviar" -Method POST -Body $contactoData -ContentType "application/json; charset=utf-8"
    Write-Host "✅ Formulario enviado: $($response2.message)" -ForegroundColor Green
    Write-Host "📊 Modo: $($response2.modo)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Error en formulario: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n🎉 PRUEBAS COMPLETADAS" -ForegroundColor Green
Write-Host "================================================" -ForegroundColor Cyan
Write-Host "📬 Revisa tu email guiro240303@gmail.com para ver los mensajes recibidos." -ForegroundColor Yellow
