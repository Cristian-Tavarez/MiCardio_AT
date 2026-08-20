# 🫀 MiCardioAT

MiCardioAT es una aplicación móvil nativa para Android diseñada para el control, registro y seguimiento clínico de pacientes en el área de cardiología.

## 🚀 Características Principales

* **Gestión de Pacientes:** Registro, edición y filtrado de datos personales (Nombre, Apellido, Edad, Sexo, Antecedentes Patológicos/Quirúrgicos, Alergias).
* **Control de Consultas / Visitas:** Módulo para registrar los signos vitales de cada cita (Presión Arterial - TA, Frecuencia Cardíaca - FC, Glucemia).
* **Búsqueda y Filtros:** Búsqueda combinada en tiempo real por nombre/apellido y filtro diario para citas programadas.
* **Historial Clínico:** Registro cronológico de consultas pasadas asociadas a cada expediente médico.

## 🛠️ Tecnologías y Arquitectura

El proyecto sigue los principios de Clean Architecture y patrones de diseño modernos recomendados por Android:

* **Lenguaje:** Kotlin

# YouTube Videos

**English**
https://youtu.be/UGil4ZfV3PQ

**Spanish**
https://youtu.be/ZZDf87I6TLI

* ## 📱 Capturas de Pantalla

* | Inicio | Pacientes | Ficha Médica |
* <img width="540" height="1204" alt="WhatsApp Image 2026-08-19 at 8 39 18 AM (2)" src="https://github.com/user-attachments/assets/7ead4cae-5bf6-4c61-9784-7462b34f65e8" />
<img width="540" height="1204" alt="WhatsApp Image 2026-08-19 at 1 03 59 PM" src="https://github.com/user-attachments/assets/bd9a0bfc-4ba7-4995-843d-60107f42a304" />
<img width="540" height="1204" alt="WhatsApp Image 2026-08-19 at 8 39 18 AM (3)" src="https://github.com/user-attachments/assets/a2212a0b-ec1d-4d88-aa12-e3a46cd737ab" />


* | Login| Historial | Ajustes |
* <img width="540" height="1204" alt="WhatsApp Image 2026-08-19 at 8 39 18 AM" src="https://github.com/user-attachments/assets/544bbb13-bb5b-4797-9a0f-2541d26c51ca" />
<img width="540" height="1204" alt="WhatsApp Image 2026-08-19 at 8 39 18 AM (4)" src="https://github.com/user-attachments/assets/2b0f451f-c248-4dd8-879d-7ac2fbc94511" />
<img width="540" height="1204" alt="WhatsApp Image 2026-08-19 at 1 04 00 PM" src="https://github.com/user-attachments/assets/c01e12df-a579-4495-9d3e-14d6ad885445" />


## 📂 Estructura del Proyecto

```text
com.example.micardioat/
│
├── data/                  # Implementación de persistencia local (Room) y DTOs
│   ├── dao/               # Objetos de Acceso a Datos (DAOs)
│   ├── database/          # Configuración de AppDatabase (Room)
│   ├── dto/               # Objetos de Transferencia de Datos
│   ├── entity/            # Entidades de la base de datos
│   └── repository/        # Implementación de los repositorios
│
├── di/                    # Módulos de inyección de dependencias con Hilt
├── domain/                # Capa de dominio (Modelos de negocio y Casos de Uso)
├── presentation/          # Pantallas de la UI y ViewModels
├── ui/                    # Componentes reutilizables de UI y Temas (Color, Type, Theme)
├── utils/                 # Manejadores de estado (Resource) y herramientas auxiliares
│
├── CardiologiaApp.kt      # Clase Application (Inicialización de Hilt)
└── MainActivity.kt        # Actividad principal y punto de entrada


## 🔧 Requisitos e Instalación

### Requisitos previos
* **Android Studio:** Ladybug / Iguana o superior
* **JDK:** 17 o superior
* **Min SDK:** 24 (Android 7.0)
* **Target SDK:** 34 / 35

## 👥 Autores / Creadores

* **Cristian Tavárez** - [GitHub](https://github.com/Cristian-Tavarez)
* **Heyson Polanco** - [GitHub](https://github.com/HeysonPolanco)

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Consulta el archivo `LICENSE` para más detalles.
