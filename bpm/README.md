# BPM con Camunda 8

El flujo base modela aprobacion de productos:

```text
Crear producto -> Solicitud pendiente -> Aprobacion -> Producto activo
```

Archivo BPMN:

```text
bpm/camunda/processes/aprobacion-producto.bpmn
```

El contenedor `camunda-zeebe` queda definido en `docker-compose.yml` para iniciar el escenario BPM.
