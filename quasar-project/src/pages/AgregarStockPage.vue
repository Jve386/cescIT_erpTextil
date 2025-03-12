<template>
  <q-page>
    <!-- Contenido para Agregar Stock -->
    <q-banner class="bg-primary text-white" rounded>
      <div class="text-h6">Agregar Artículos al Stock</div>
    </q-banner>

    <q-form @submit.prevent="agregarStock">
      <q-input v-model="crearStock.idArticulo" label="ID Artículo" type="number" required />
      <q-input v-model="crearStock.idAlmacen" label="ID Almacén" type="number" required />
      <q-input v-model="crearStock.cantidad" label="Cantidad" type="number" min="1" required />
      <q-btn label="Agregar Stock" color="primary" type="submit" class="q-mt-md" />
    </q-form>
  </q-page>
</template>

<script>
export default {
  data() {
    return {
      crearStock: {
        idArticulo: null,
        idAlmacen: null,
        cantidad: null,
      },
    };
  },
  mounted() {
    console.log('Initial crearStock:', JSON.parse(JSON.stringify(this.crearStock)));
  },
  methods: {
    async agregarStock() {
      // Aseguramos que los valores sean números
      const datosEnviar = {

        idArticulo: Number(this.crearStock.idArticulo),
        idAlmacen: Number(this.crearStock.idAlmacen),
        cantidad: Number(this.crearStock.cantidad),
      };

      console.log('Datos a enviar (copia profunda):', datosEnviar);
      console.log('idArticulo:', datosEnviar.idArticulo);
      console.log('idAlmacen:', datosEnviar.idAlmacen);

      try {
        // Enviamos el StockDTO a la API y capturamos la respuesta
        const response = await this.$api.post('/stocks', datosEnviar);

        // Puedes usar la respuesta para algo si lo deseas
        console.log('Respuesta de la API:', response);

        // Notificación de éxito
        this.$q.notify({
          color: 'positive',
          message: 'Stock agregado correctamente.',
          icon: 'check',
        });

        // Limpiamos el formulario después de la solicitud
        this.crearStock = {
          idArticulo: null,
          idAlmacen: null,
          cantidad: null,
        };
        console.log('After reset, crearStock:', JSON.parse(JSON.stringify(this.crearStock)));
      } catch (error) {
        // Manejo de error si algo sale mal
        console.error('Error al agregar el stock:', error);
        this.$q.notify({
          color: 'negative',
          message: 'Hubo un error al agregar el stock.',
          icon: 'warning',
        });
      }
    }
  }


};
</script>