# Configuración de Gmail para AstroDev Academy

## Pasos para configurar el envío de emails desde el formulario de contacto:

### 1. Configurar Gmail para permitir aplicaciones menos seguras

#### Opción A: Usar contraseña de aplicación (RECOMENDADO)
1. Ve a tu cuenta de Google: https://myaccount.google.com/
2. En el menú izquierdo, selecciona "Seguridad"
3. En "Cómo iniciar sesión en Google", asegúrate de que la "Verificación en 2 pasos" esté activada
4. Una vez activada la verificación en 2 pasos, verás una nueva opción "Contraseñas de aplicaciones"
5. Haz clic en "Contraseñas de aplicaciones"
6. Selecciona "Correo" como aplicación y "Otro" como dispositivo
7. Escribe "AstroDev Academy" como nombre
8. Google generará una contraseña de 16 caracteres
9. Copia esta contraseña (sin espacios)

#### Opción B: Permitir aplicaciones menos seguras (NO RECOMENDADO)
1. Ve a https://myaccount.google.com/lesssecureapps
2. Activa "Permitir aplicaciones menos seguras"

### 2. Configurar la aplicación

#### Método 1: Variable de entorno (RECOMENDADO)
1. Configura la variable de entorno en tu sistema:
   - **Windows PowerShell:** `$env:GMAIL_APP_PASSWORD="tu_contraseña_de_aplicacion"`
   - **Windows CMD:** `set GMAIL_APP_PASSWORD=tu_contraseña_de_aplicacion`
   - **Linux/Mac:** `export GMAIL_APP_PASSWORD=tu_contraseña_de_aplicacion`

2. Reinicia tu aplicación Spring Boot

#### Método 2: Archivo de propiedades (TEMPORAL)
1. Edita el archivo `src/main/resources/application.properties`
2. Cambia la línea:
   ```
   spring.mail.password=${GMAIL_APP_PASSWORD:password_temporal}
   ```
   por:
   ```
   spring.mail.password=tu_contraseña_de_aplicacion
   ```
3. **IMPORTANTE:** NO subas este archivo al repositorio con tu contraseña real

### 3. Verificar configuración
1. Ejecuta la aplicación: `mvn spring-boot:run`
2. Ve a http://localhost:8080/astrodev/contacto
3. Llena y envía el formulario de contacto
4. Verifica que llegue el email a guiro240303@gmail.com

### 4. Solución de problemas

#### Error "Authentication failed"
- Verifica que la contraseña de aplicación sea correcta
- Asegúrate de que la verificación en 2 pasos esté activada
- Verifica que el email en `spring.mail.username` sea correcto

#### Error "Connection refused"
- Verifica tu conexión a internet
- Asegúrate de que el puerto 587 no esté bloqueado por firewall

#### Emails no llegan
- Revisa la carpeta de spam/correo no deseado
- Verifica que el email destinatario sea correcto en application.properties
- Revisa los logs de la aplicación para ver errores

### 5. Configuración de producción
Para producción, se recomienda:
1. Usar variables de entorno para todas las credenciales
2. Considerar usar un servicio de email como SendGrid, Mailgun, o Amazon SES
3. Implementar límites de tasa para prevenir spam
4. Agregar CAPTCHA al formulario

### 6. Personalización adicional
- Puedes modificar el template de email en `EmailService.java`
- Agregar más campos al formulario editando `ContactoDTO.java`
- Personalizar las validaciones en el JavaScript y el backend
