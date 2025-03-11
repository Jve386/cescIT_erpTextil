<template>
  <q-page padding>
    <q-banner class="bg-primary text-white" rounded>
      <div class="text-h6">Realizar Venta</div>
    </q-banner>

    <!-- Formulario para realizar una venta -->
    <q-form @submit.prevent="realizarVenta" class="q-gutter-md q-mt-lg">
      <div class="row q-col-gutter-md">
        <div class="col-12 col-md-6">
          <q-input
            v-model="venta.idCliente"
            label="ID Cliente"
            type="number"
            outlined
            required
          />
        </div>
        <div class="col-12 col-md-6">
          <q-input
            v-model="venta.nombreCliente"
            label="Nombre del Cliente"
            type="text"
            outlined
            required
          />
        </div>
      </div>

      <div class="row q-col-gutter-md">
        <div class="col-12 col-md-6">
          <q-input
            v-model="venta.fecha"
            label="Fecha de la Venta"
            type="date"
            outlined
            required
          />
        </div>
        <div class="col-12 col-md-6">
          <q-input
            v-model="venta.numeroTicket"
            label="Número de Ticket"
            type="text"
            outlined
            required
          />
        </div>
      </div>

      <!-- Sección para Artículos -->
      <div class="q-my-md">
        <div class="text-h6">Artículos</div>

        <div v-for="(detalle, index) in venta.detalles" :key="index" class="q-mb-md">
          <q-card bordered>
            <q-card-section>
              <div class="row q-col-gutter-md">
                <div class="col-12 col-md-6">
                  <q-input
                    v-model.number="detalle.idArticulo"
                    label="ID Artículo"
                    type="number"
                    outlined
                    required
                  />
                </div>
                <div class="col-12 col-md-6">
                  <q-input
                    v-model="detalle.nombreProducto"
                    label="Nombre del Producto"
                    type="text"
                    outlined
                    required
                  />
                </div>
              </div>

              <div class="row q-col-gutter-md q-mt-sm">
                <div class="col-6 col-md-3">
                  <q-input
                    v-model="detalle.talla"
                    label="Talla"
                    type="text"
                    outlined
                  />
                </div>
                <div class="col-6 col-md-3">
                  <q-input
                    v-model="detalle.color"
                    label="Color"
                    type="text"
                    outlined
                  />
                </div>
                <div class="col-6 col-md-2">
                  <q-input
                    v-model.number="detalle.cantidad"
                    label="Cantidad"
                    type="number"
                    outlined
                    required
                    @change="calcularPrecioTotal(index)"
                  />
                </div>
                <div class="col-6 col-md-2">
                  <q-input
                    v-model.number="detalle.precioUnitario"
                    label="Precio Unit."
                    type="number"
                    outlined
                    required
                    @change="calcularPrecioTotal(index)"
                  />
                </div>
                <div class="col-12 col-md-2">
                  <q-input
                    v-model.number="detalle.precioTotal"
                    label="Precio Total"
                    type="number"
                    outlined
                    readonly
                  />
                </div>
              </div>
            </q-card-section>

            <q-card-actions align="right">
              <q-btn
                flat
                color="negative"
                icon="delete"
                @click="eliminarDetalle(index)"
                label="Eliminar"
              />
            </q-card-actions>
          </q-card>
        </div>

        <div class="q-mt-md">
          <q-btn
            color="primary"
            icon="add"
            label="Añadir Artículo"
            @click="agregarDetalle"
          />
        </div>
      </div>

      <!-- Totales -->
      <q-card bordered class="q-mt-md">
        <q-card-section>
          <div class="row q-col-gutter-md">
            <div class="col-12 col-md-6">
              <q-input
                v-model.number="venta.totalSinIVA"
                label="Total sin IVA"
                type="number"
                outlined
                readonly
              />
            </div>
            <div class="col-12 col-md-6">
              <q-input
                v-model.number="venta.totalConIVA"
                label="Total con IVA (21%)"
                type="number"
                outlined
                readonly
              />
            </div>
          </div>
        </q-card-section>
      </q-card>

      <div class="q-mt-lg">
        <q-btn
          label="Realizar Venta"
          color="secondary"
          type="submit"
          icon="shopping_cart"
          :loading="loading"
          :disable="venta.detalles.length === 0"
        />
      </div>
    </q-form>
  </q-page>
</template>

<script>
export default {
  data() {
    return {
      loading: false,
      venta: {
        idCliente: null,
        nombreCliente: '',
        fecha: '',
        numeroTicket: '',
        estado: 'pendiente', // Por defecto en pendiente
        totalSinIVA: 0,
        totalConIVA: 0,
        detalles: []
      }
    };
  },
  methods: {
    agregarDetalle() {
      this.venta.detalles.push({
        idArticulo: null,
        nombreProducto: '',
        talla: '',
        color: '',
        cantidad: 1,
        precioUnitario: 0,
        precioTotal: 0
      });
    },

    eliminarDetalle(index) {
      this.venta.detalles.splice(index, 1);
      this.calcularTotales();
    },

    calcularPrecioTotal(index) {
      const detalle = this.venta.detalles[index];
      detalle.precioTotal = detalle.cantidad * detalle.precioUnitario;
      this.calcularTotales();
    },

    calcularTotales() {
      // Calcular total sin IVA (suma de todos los precios totales)
      this.venta.totalSinIVA = this.venta.detalles.reduce((total, detalle) => {
        return total + (detalle.precioTotal || 0);
      }, 0);

      // Calcular total con IVA (21%)
      this.venta.totalConIVA = this.venta.totalSinIVA * 1.21;
    },

    async realizarVenta() {
      if (this.venta.detalles.length === 0) {
        this.$q.notify({
          color: 'warning',
          message: 'Debe agregar al menos un artículo a la venta.',
          icon: 'warning',
        });
        return;
      }

      this.loading = true;
      try {
        await this.$api.post('/api/ventas', this.venta);
        this.$q.notify({
          color: 'positive',
          message: 'Venta realizada correctamente.',
          icon: 'check',
        });

        // Reiniciar el formulario
        this.venta = {
          idCliente: null,
          nombreCliente: '',
          fecha: '',
          numeroTicket: '',
          estado: 'pendiente',
          totalSinIVA: 0,
          totalConIVA: 0,
          detalles: []
        };
      } catch (error) {
        console.error('Error al realizar la venta:', error);
        let errorMessage = 'Hubo un error al realizar la venta.';

        // Si hay detalles del error, mostrarlos
        if (error.response && error.response.data) {
          errorMessage += ' ' + (error.response.data.message || JSON.stringify(error.response.data));
        }

        this.$q.notify({
          color: 'negative',
          message: errorMessage,
          icon: 'warning',
        });
      } finally {
        this.loading = false;
      }
    }
  },
  created() {
    // Si estamos en un día actual, establecer la fecha actual
    if (!this.venta.fecha) {
      const today = new Date();
      const year = today.getFullYear();
      const month = String(today.getMonth() + 1).padStart(2, '0');
      const day = String(today.getDate()).padStart(2, '0');
      this.venta.fecha = `${year}-${month}-${day}`;
    }

    // Generar un número de ticket por defecto basado en la fecha y hora actual
    if (!this.venta.numeroTicket) {
      const timestamp = new Date().getTime();
      this.venta.numeroTicket = `TK-${timestamp}`;
    }
  }
};
</script>
