<template>
  <q-page>
    <q-banner class="bg-secondary text-white q-mt-md" rounded>
      <div class="text-h6">Consultar Stock por ID Artículo</div>
    </q-banner>

    <q-form @submit.prevent="consultarStock">
      <q-input v-model="idArticuloConsulta" label="ID Artículo para consultar" type="number" required />
      <q-btn label="Consultar Stock" color="secondary" type="submit" class="q-mt-md" />
    </q-form>

    <!-- Mostrar resultados de la consulta -->
    <div v-if="stockConsulta.length > 0">
      <q-card class="q-mt-md">
        <q-card-section>
          <div class="text-h6">Stock del Artículo ID: {{ idArticuloConsulta }}</div>
          <div v-for="(stock, index) in stockConsulta" :key="index">
            <div><strong>Cantidad en Almacén {{ stock.idAlmacen }}:</strong> {{ stock.cantidad }}</div>
          </div>
        </q-card-section>
      </q-card>
    </div>
    <div v-else-if="stockConsulta === null">
      <p>No se encontró stock para este artículo.</p>
    </div>
  </q-page>
</template>

<script>
export default {
  data() {
    return {
      idArticuloConsulta: null,
      stockConsulta: [], // Almacena los datos consultados
    };
  },
  methods: {
    async consultarStock() {
      try {
        console.log('Consultando stock para artículo ID:', this.idArticuloConsulta);
        console.log('URL completa:', this.$api.defaults.baseURL + '/stocks/articulo/' + this.idArticuloConsulta);
        const response = await this.$api.get(`/stocks/articulo/${this.idArticuloConsulta}`);
        console.log('Respuesta del servidor:', response.data);
        this.stockConsulta = response.data;
      } catch (error) {
        console.error('Error al consultar el stock:', error);
        console.log('Detalles del error:', error.response ? error.response.data : 'No hay detalles');
        this.$q.notify({
          color: 'negative',
          message: 'Hubo un error al consultar el stock.',
          icon: 'warning',
        });
        this.stockConsulta = [];
      }
    },
  },
};
</script>
