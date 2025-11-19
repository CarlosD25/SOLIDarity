# 📌 Plataforma Solidaria  
**Sistema para la gestión, validación y donación en campañas solidarias**  
_Basado en la norma IEEE/ISO/IEC 29148:2018_

---

## 📄 Descripción general  
La **Plataforma Solidaria** es un sistema web y multidispositivo orientado a facilitar la creación, validación y gestión de campañas solidarias. Proporciona herramientas para donantes, beneficiarios y administradores garantizando transparencia, seguridad y usabilidad.  
El desarrollo sigue el proceso **RUP** y utiliza **PostgreSQL** bajo el paradigma de **Programación Orientada a Objetos**, sin frameworks backend.

---

## 📘 Información del documento  
- **Versión del documento:** 1.0  
- **Fecha:** 5 de septiembre de 2025  
- **Curso:** Arquitectura de Software  
- **Docente:** Ing. Carlos Henríquez Miranda PhD  
- **Institución:** Universidad del Magdalena – Facultad de Ingeniería  

---

## 👥 Equipo de desarrollo  
- Dylan De Vega Torres  
- Cristian David Mendoza Rocha  
- Daniel Esteban Puerta Marrugo  
- Carlos Daniel Sánchez Palomino  
- Yesid David Soto Pacheco  

---

# 1. Introducción  

## 1.1 Propósito  
Definir de manera clara y verificable los requisitos funcionales y no funcionales de la **Plataforma Solidaria**, garantizando trazabilidad, calidad y alineación con las necesidades de los usuarios y actores involucrados.

## 1.2 Alcance  
El sistema permitirá:  
- Registro y gestión de campañas solidarias.  
- Donación en dinero y especie.  
- Validación y supervisión de campañas por administradores.  
- Notificaciones, reportes y comprobantes.  
- Acceso multidispositivo (web y móvil).  
- Integración con pasarelas de pago y servicios externos.

## 1.3 Definiciones y acrónimos  
- **RF:** Requisito Funcional  
- **RNF:** Requisito No Funcional  
- **Beneficiario:** Usuario que registra campañas  
- **Donante:** Usuario que realiza aportes  
- **Administrador:** Usuario que valida y supervisa  
- **Domiciliario:** Encargado de entregas de donativos físicos  
- **Entidad social:** Organizaciones que gestionan campañas colectivas  
- **Equipo de verificación:** Grupo encargado de validar la veracidad de campañas

## 1.4 Referencias  
- IEEE/ISO/IEC 29148:2018  
- IEEE 830:1998  

---

# 2. Descripción general del sistema  

## 2.1 Perspectiva  
Sistema centralizado accesible desde web y móvil, compuesto por módulos para:  
- Gestión de usuarios  
- Gestión de campañas  
- Donaciones  
- Seguridad  
- Validaciones  
- Reportes  
- Notificaciones  

## 2.2 Funciones principales  
- Registro y gestión de campañas  
- Validación y aprobación de campañas  
- Donaciones monetarias y en especie  
- Generación de reportes y comprobantes  
- Notificaciones automáticas  
- Gestión de fraudes  
- Roles y permisos  

## 2.3 Usuarios del sistema  
- Beneficiario  
- Donante  
- Administrador  
- Domiciliario  
- Entidad social  
- Equipo de verificación  

## 2.4 Restricciones  
- Uso obligatorio de RUP  
- Base de datos PostgreSQL  
- Paradigma POO  
- No se permiten frameworks backend  
- Interfaz adaptada a contexto social y cultural  

## 2.5 Supuestos  
- Los usuarios poseen acceso a internet  
- Dependencia de pasarelas de pago externas  
- Dependencia de servicios de autenticación y correo  

---

# 3. Requisitos específicos  

## 3.1 Requisitos funcionales (RF)  
Incluyen, entre otros:  
- RF01: Registro de campañas con imágenes y videos  
- RF02: Validación de campañas por administradores  
- RF05–RF06: Donación en dinero y en especie  
- RF07: Generación de comprobantes digitales  
- RF08: Visualización del progreso de donaciones  
- RF09–RF10: Notificaciones a beneficiarios y donantes  
- RF11: Gestión de reportes de fraude  
- RF12: Informes de transparencia  
- RF13–RF14: Registro y edición de perfiles  
- RF16: Cierre de campañas  
- RF17: Denuncia de campañas fraudulentas  
- RF19–RF20: Donaciones anónimas e historial de aportes  
- RF21: Exportación de reportes en PDF  
- RF23–RF24: Recordatorios y notificaciones automáticas  
- RF25: Cierre de sesión por inactividad  
- RF27–RF28: Validación de métodos de pago y notificación de fallos  
- RF29–RF30: Campañas destacadas y compartir en redes sociales  

_El documento contiene un total de 30 RF completos._

## 3.2 Requisitos no funcionales (RNF)  
- **Usabilidad:** interfaz intuitiva, accesible y multidispositivo  
- **Rendimiento:** tiempos de respuesta < 3 segundos  
- **Confiabilidad:** disponibilidad del 99%, recuperación automática  
- **Seguridad:** autenticación por correo, control por roles  
- **Mantenibilidad:** POO, documentación estándar RUP  
- **Restricciones:** no uso de frameworks backend, lenguaje claro  

## 3.3 Requisitos de interfaz  
### Interfaz de usuario  
- Web responsive y aplicación móvil  
- Formularios simples y navegación clara  
- Notificaciones visuales y auditivas  

### Interfaz externa  
- Pasarelas de pago  
- Servicios de correo  
- Redes sociales  

---

# 4. Trazabilidad  
Cada requisito funcional será asociado con casos de uso, escenarios y pruebas de aceptación mediante una matriz de trazabilidad desarrollada en la fase de diseño.

---

