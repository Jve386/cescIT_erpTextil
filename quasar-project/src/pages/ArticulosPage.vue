<template>
  <q-page>
    <!-- Banner principal -->
    <q-banner class="bg-primary text-white" rounded>
      <div class="text-h6">Agregar Artículos al Stock</div>
    </q-banner>

    <!-- Formulario para agregar stock -->
    <q-form @submit.prevent="agregarStock">
      <q-input v-model="crearStock.idArticulo" label="ID Artículo" type="number" required />
      <q-input v-model="crearStock.idAlmacen" label="ID Almacén" type="number" required />
      <q-input v-model="crearStock.cantidad" label="Cantidad" type="number" min="1" required />

      <q-btn label="Agregar Stock" color="primary" type="submit" class="q-mt-md" />
    </q-form>

    <!-- Sección para consultar stock por ID artículo -->
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
      // Formulario para agregar stock
      crearStock: {
        idArticulo: null,
        idAlmacen: null,
        cantidad: null,
      },
      // Para consultar stock
      idArticuloConsulta: null,
      stockConsulta: [], // Almacena los datos consultados
    };
  },
  methods: {
    // Método para agregar stock
    async agregarStock() {
      try {
        const response = await this.$api.post('/stocks', this.crearStock);
        console.log(response.data);

        this.$q.notify({
          color: 'positive',
          message: 'Stock agregado correctamente.',
          icon: 'check',
        });

        // Limpiar el formulario
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

    // Método para consultar stock por ID Artículo
    async consultarStock() {
      try {
        const response = await this.$api.get(`/stocks/articulo/${this.idArticuloConsulta}`);

        // Aquí, el backend debe devolver un arreglo con los registros de stock
        this.stockConsulta = response.data; // Suponemos que la respuesta es una lista con las cantidades y almacenes
      } catch (error) {
        console.error('Error al consultar el stock:', error);

        this.$q.notify({
          color: 'negative',
          message: 'Hubo un error al consultar el stock.',
          icon: 'warning',
        });

        this.stockConsulta = []; // Limpiar en caso de error
      }
    },
  },
};
</script>
