<template>
  <q-page>
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
// Using Options API approach with Quasar
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
        const response = await this.$api.post('/stocks', this.crearStock);
        console.log(response.data);

        // Use $q from this
        this.$q.notify({
          color: 'positive',
          message: 'Stock agregado correctamente.',
          icon: 'check',
        });

        // Limpiar el formulario después de agregar con éxito
        this.crearStock = {
          idArticulo: null,
          idAlmacen: null,
          cantidad: null,
        };
      } catch (error) {
        console.error('Error al agregar el stock:', error);

        // Check if $q is available and use it safely
        if (this.$q && typeof this.$q.notify === 'function') {
          this.$q.notify({
            color: 'negative',
            message: 'Hubo un error al agregar el stock.',
            icon: 'warning',
          });
        } else {
          // Fallback if notify isn't available
          alert('Hubo un error al agregar el stock.');
        }
      }
    }
  },
};
</script>
