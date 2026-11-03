# Dasher Profit Bot (Android prototype)

Aplicación Android de prueba con interfaz negro/verde y datos guardados localmente. Incluye filtros configurables, análisis manual de ofertas, registro de ingresos/gastos, exportación y seguimiento GPS mientras la app está activa.

## APK en GitHub Actions
En GitHub, abre **Actions > Compilar APK Android**. Comprueba si el flujo de trabajo de la rama main termina correctamente. Si termina en verde, abre la ejecución y descarga el artefacto **DasherProfitBot-debug-apk**. Extrae el ZIP y utiliza el APK de prueba en tu Android.

No se ha verificado una compilación ni la instalación en un teléfono.

## Límites
- Sin integración oficial de datos de ofertas de DoorDash ni Uber Eats.
- Sin lectura de otras aplicaciones, ni aceptación automática de pedidos.
- GPS no funciona de forma garantizada con la app cerrada o en segundo plano.
- Sin alertas flotantes ni notificaciones persistentes.
- Los precios de $19.99 y $39.99 son informativos; no hay integración de Stripe ni cobros reales.
- Falta seguridad de producción, autenticación, pruebas y publicación en tiendas.

No introduzcas contraseñas, claves ni información bancaria en el repositorio.
