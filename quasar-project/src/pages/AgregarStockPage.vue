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
  methods: {
    async agregarStock() {
      try {
        await this.$api.post('/stocks', this.crearStock);
        this.$q.notify({
          color: 'positive',
          message: 'Stock agregado correctamente.',
          icon: 'check',
        });

        this.crearStock = {
          idArticulo: null,
          idAlmacen: null,
          cantidad: null,
        };
      } catch (error) {
        console.error('Error al agregar el stock:', error);
        this.$q.notify({
          color: 'negative',
          message: 'Hubo un error al agregar el stock.',
          icon: 'warning',
        });
      }
    },
  },
};
</script>
