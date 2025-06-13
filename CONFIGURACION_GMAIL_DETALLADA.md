# 🔧 CONFIGURACIÓN GMAIL PARA ENVÍO DE EMAILS

## 📋 CHECKLIST DE CONFIGURACIÓN

### 1. ✅ CONFIGURACIÓN DE CUENTA GMAIL

#### **Verificar Autenticación de 2 Factores:**
1. Ve a [myaccount.google.com](https://myaccount.google.com)
2. Selecciona **Seguridad** → **Verificación en 2 pasos**
3. Asegúrate de que esté **ACTIVADA**

#### **Crear/Verificar Contraseña de Aplicación:**
1. Ve a [myaccount.google.com](https://myaccount.google.com)
2. Seguridad → Verificación en 2 pasos → **Contraseñas de aplicaciones**
3. Selecciona **Correo** → **Otra (nombre personalizado)**
4. Nombra: "AstroDev Academy SMTP"
5. Copia la contraseña generada (16 caracteres)
6. **IMPORTANTE**: Esta contraseña solo se muestra UNA VEZ

### 2. ✅ CONFIGURACIÓN DE RED WINDOWS

#### **Configurar Firewall de Windows:**
```powershell
# Ejecutar como Administrador en PowerShell:

# Permitir puertos SMTP salientes
New-NetFirewallRule -DisplayName "SMTP Gmail 587" -Direction Outbound -Protocol TCP -LocalPort 587 -Action Allow
New-NetFirewallRule -DisplayName "SMTP Gmail 465" -Direction Outbound -Protocol TCP -LocalPort 465 -Action Allow

# Verificar reglas
Get-NetFirewallRule -DisplayName "*SMTP*"
```

#### **Configurar Proxy (si aplica):**
Si estás en una red corporativa o universitaria:

1. **Verificar configuración proxy actual:**
```powershell
netsh winhttp show proxy
```

2. **Si hay proxy, configurar bypass para Gmail:**
```powershell
# Configurar bypass para servidores Gmail
netsh winhttp set proxy proxy-server="tu-proxy:puerto" bypass-list="*.gmail.com;*.google.com;smtp.gmail.com"
```

### 3. ✅ CONFIGURACIÓN DE RED CORPORATIVA/UNIVERSITARIA

#### **Para redes universitarias (UNSM):**
Es común que las universidades bloqueen puertos SMTP. Contacta a:

- **Mesa de ayuda IT** de la universidad
- Solicitar **habilitación puerto 587** para Gmail SMTP
- Proporcionar justificación: "Proyecto académico de desarrollo web"

#### **Alternativas si no se puede abrir puerto 587:**

1. **Usar VPN:**
   - Instalar VPN confiable (ProtonVPN, WindScribe gratuitos)
   - Conectar y probar conectividad

2. **Usar hotspot móvil:**
   - Conectar a datos móviles temporalmente
   - Probar funcionamiento del sistema

### 4. ✅ VERIFICAR CONECTIVIDAD

#### **Test básico de conectividad:**
```powershell
# Probar conectividad a Gmail SMTP
Test-NetConnection -ComputerName smtp.gmail.com -Port 587
```

**Resultado esperado:**
- `TcpTestSucceeded: True` ✅
- Si es `False` ❌ = Puerto bloqueado

#### **Test con Telnet:**
```powershell
# Habilitar cliente Telnet (una vez)
Enable-WindowsOptionalFeature -Online -FeatureName TelnetClient

# Probar conexión manual
telnet smtp.gmail.com 587
```

**Respuesta esperada:**
```
220 smtp.gmail.com ESMTP...
```

### 5. ✅ SOLUCIÓN DE PROBLEMAS COMUNES

#### **Error: "Connection timeout"**
- 🔍 **Causa**: Puerto 587 bloqueado por firewall/ISP
- 🔧 **Solución**: Configurar firewall o usar VPN

#### **Error: "Authentication failed"**
- 🔍 **Causa**: Contraseña de aplicación incorrecta
- 🔧 **Solución**: Generar nueva contraseña de aplicación

#### **Error: "SSL/TLS handshake failed"**
- 🔍 **Causa**: Problemas de certificados
- 🔧 **Solución**: Verificar configuración STARTTLS

### 6. ✅ CONFIGURACIÓN RECOMENDADA PARA DESARROLLO

#### **Variables de entorno (.env):**
```properties
# Gmail SMTP Configuration
GMAIL_APP_PASSWORD=tu_contraseña_aplicacion_16_caracteres
EMAIL_DEMO_MODE=false

# Opcional: Configuración de red
JAVA_OPTS=-Djava.net.useSystemProxies=true
```

#### **Configuración alternativa (puerto 465):**
Si el puerto 587 está bloqueado, puedes intentar el 465:

En `application.properties`:
```properties
spring.mail.port=465
spring.mail.properties.mail.smtp.ssl.enable=true
spring.mail.properties.mail.smtp.starttls.enable=false
```

### 7. ✅ COMANDOS DE VERIFICACIÓN RÁPIDA

```powershell
# 1. Verificar DNS
nslookup smtp.gmail.com

# 2. Verificar conectividad
Test-NetConnection smtp.gmail.com -Port 587

# 3. Verificar proxy
netsh winhttp show proxy

# 4. Verificar firewall
Get-NetFirewallRule | Where-Object {$_.DisplayName -like "*587*"}
```

---

## 🚨 NOTA IMPORTANTE PARA REDES UNIVERSITARIAS

Las universidades suelen bloquear puertos SMTP por seguridad. Si no puedes configurar la red institucional:

1. **Usa tu conexión móvil** para desarrollo
2. **Solicita excepción** al departamento de IT
3. **Considera servicios alternativos** como SendGrid o AWS SES para producción

---

## 📞 CONTACTOS DE SOPORTE UNSM

- **Mesa de ayuda IT**: [contacto IT UNSM]
- **Solicitud**: "Habilitar puerto 587 TCP saliente para proyecto académico"
- **Justificación**: "Desarrollo de aplicación web con funcionalidad de email"
