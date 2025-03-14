<template>
  <q-page padding>
    <q-banner class="bg-primary text-white" rounded>
      <div class="text-h6">Realizar Venta</div>
    </q-banner>

    <!-- Formulario para realizar una venta -->
    <q-form @submit.prevent>
      <div class="row q-col-gutter-md">
        <div class="col-12 col-md-4">
          <q-card>
            <q-card-section>
              <div class="text-h6">Datos del Cliente</div>
              <div class="row q-col-gutter-sm q-mt-md">
                <div class="col-12">
                  <q-input
                    v-model="venta.idCliente"
                    label="ID del Cliente"
                    type="number"
                    @blur="buscarCliente"
                    :rules="[val => !!val || 'ID del cliente es requerido']"
                  />
                </div>
                <div class="col-12">
                  <q-input
                    v-model="venta.nombreCliente"
                    label="Nombre del Cliente"
                    readonly
                    class="q-mb-sm"
                  />
                  <q-btn
                    color="primary"
                    label="Nuevo Cliente"
                    @click="mostrarDialogoNuevoCliente"
                  />
                </div>
              </div>
            </q-card-section>
          </q-card>
        </div>
        <div class="col-12 col-md-4">
          <q-input v-model="venta.fecha" label="Fecha de la Venta" type="date" outlined required />
        </div>
        <div class="col-12 col-md-4">
          <q-input v-model="venta.numeroTicket" label="Número de Ticket" type="text" outlined required />
        </div>
      </div>

      <div class="row q-col-gutter-md">
        <div class="col-12 col-md-4">
          <q-select v-model="venta.idAlmacen" :options="almacenes" option-value="id" option-label="nombre"
            label="Almacén" outlined required emit-value map-options :loading="cargandoAlmacenes" />
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
                  <q-input v-model.number="detalle.idArticulo" label="ID Artículo" type="number" outlined required
                    @blur="buscarArticulo(index)" :loading="detalle.cargando" />
                </div>
                <div class="col-12 col-md-6">
                  <q-input v-model="detalle.nombreProducto" label="Nombre del Producto" type="text" outlined required
                    :readonly="detalle.datosAutomaticos" />
                </div>
              </div>

              <div class="row q-col-gutter-md q-mt-sm">
                <div class="col-6 col-md-3">
                  <q-input v-model="detalle.talla" label="Talla" type="text" outlined
                    :readonly="detalle.datosAutomaticos" />
                </div>
                <div class="col-6 col-md-3">
                  <q-input v-model="detalle.color" label="Color" type="text" outlined
                    :readonly="detalle.datosAutomaticos" />
                </div>
                <div class="col-6 col-md-2">
                  <q-input v-model.number="detalle.cantidad" label="Cantidad" type="number" outlined required
                    @change="calcularPrecioTotal(index)" />
                </div>
                <div class="col-6 col-md-2">
                  <q-input v-model.number="detalle.precioUnitario" label="Precio Unit." type="number" outlined required
                    @change="calcularPrecioTotal(index)" :readonly="detalle.datosAutomaticos" />
                </div>
                <div class="col-12 col-md-2">
                  <q-input v-model.number="detalle.precioTotal" label="Precio Total" type="number" outlined readonly />
                </div>
              </div>
            </q-card-section>

            <q-card-actions align="right">
              <q-btn flat color="negative" icon="delete" @click="eliminarDetalle(index)" label="Eliminar" />
            </q-card-actions>
          </q-card>
        </div>

        <div class="q-mt-md">
          <q-btn color="primary" icon="add" label="Añadir Artículo" @click="agregarDetalle" />
        </div>
      </div>

      <!-- Totales -->
      <q-card bordered class="q-mt-md">
        <q-card-section>
          <div class="row q-col-gutter-md">
            <div class="col-12 col-md-6">
              <q-input v-model.number="venta.totalSinIVA" label="Total sin IVA" type="number" outlined readonly />
            </div>
            <div class="col-12 col-md-6">
              <q-input v-model.number="venta.totalConIVA" label="Total con IVA (21%)" type="number" outlined readonly />
            </div>
          </div>
        </q-card-section>
      </q-card>

      <div class="q-mt-lg">
        <div class="row q-col-gutter-md">
          <div class="col-12 col-md-6">
            <q-btn label="Guardar Venta" color="primary" icon="save" @click="procesarVenta('pendiente')" :loading="loading" :disable="venta.detalles.length === 0" class="full-width" />
          </div>
          <div class="col-12 col-md-6">
            <q-btn label="Realizar Venta" color="secondary" icon="shopping_cart" @click="procesarVenta('completada')" :loading="loading" :disable="venta.detalles.length === 0" class="full-width" />
          </div>
        </div>
      </div>
    </q-form>

    <!-- Diálogo para nuevo cliente -->
    <q-dialog v-model="dialogoNuevoCliente" persistent>
      <q-card style="min-width: 350px">
        <q-card-section>
          <div class="text-h6">Nuevo Cliente</div>
        </q-card-section>

        <q-card-section class="q-pt-none">
          <q-form @submit.prevent="crearCliente" class="q-gutter-md">
            <q-input
              v-model="nuevoCliente.nombre"
              label="Nombre"
              :rules="[val => !!val || 'El nombre es requerido']"
            />
            <q-input
              v-model="nuevoCliente.email"
              label="Email"
              type="email"
              :rules="[val => !!val || 'El email es requerido']"
            />
            <q-input
              v-model="nuevoCliente.telefono"
              label="Teléfono"
            />
          </q-form>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Cancelar" color="primary" v-close-popup />
          <q-btn flat label="Crear" color="primary" @click="crearCliente" :loading="creandoCliente" />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script>
export default {
  name: 'VentasPage',
  data() {
    return {
      loading: false,
      cargandoCliente: false,
      cargandoAlmacenes: false,
      maxRetries: 5,
      retryDelay: 2000,
      dialogoNuevoCliente: false,
      creandoCliente: false,
      nuevoCliente: {
        nombre: '',
        email: '',
        telefono: ''
      },
      almacenes: [
        { id: 1, nombre: 'Tienda Central' },
        { id: 2, nombre: 'Almacén Norte' }
      ],
      venta: {
        idCliente: null,
        nombreCliente: '',
        fecha: '',
        numeroTicket: '',
        estado: 'pendiente',
        idAlmacen: 1,
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
        precioTotal: 0,
        cargando: false,
        datosAutomaticos: false
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

    async cargarAlmacenes(retryCount = 0) {
      this.cargandoAlmacenes = true;
      try {
        const response = await this.$api.get('/almacenes');
        if (response.data && response.data.length > 0) {
          this.almacenes = response.data;
        }
        this.cargandoAlmacenes = false;
      } catch (error) {
        console.error('Error al cargar almacenes:', error);

        // Si aún no hemos alcanzado el máximo de intentos, reintentamos
        if (retryCount < this.maxRetries) {
          console.log(`Reintentando cargar almacenes en ${this.retryDelay / 1000} segundos... (Intento ${retryCount + 1}/${this.maxRetries})`);
          this.cargandoAlmacenes = false;

          // Esperar antes de reintentar
          await new Promise(resolve => setTimeout(resolve, this.retryDelay));

          // Reintentar la carga
          await this.cargarAlmacenes(retryCount + 1);
        } else {
          this.cargandoAlmacenes = false;
          console.log('Se alcanzó el máximo de intentos. Usando almacenes por defecto.');
          // Notificar al usuario
          this.$q.notify({
            color: 'warning',
            message: 'No se pudieron cargar los almacenes. Usando valores por defecto.',
            timeout: 5000
          });
        }
      }
    },

    async buscarCliente() {
      if (!this.venta.idCliente) return;

      this.cargandoCliente = true;
      try {
        console.log('Buscando cliente con ID:', this.venta.idCliente);
        const response = await this.$api.get(`/clientes/${this.venta.idCliente}`);
        console.log('Respuesta del servidor:', response.data);
        if (response.data) {
          this.venta.nombreCliente = response.data.nombre;
        }
      } catch (error) {
        console.error('Error al buscar cliente:', error);
        console.log('Detalles del error:', error.response ? error.response.data : 'No hay detalles');
        console.log('Estado del error:', error.response ? error.response.status : 'No hay estado');
        console.log('Headers del error:', error.response ? error.response.headers : 'No hay headers');
        
        this.$q.notify({
          color: 'negative',
          message: 'No se encontró el cliente con ese ID',
          icon: 'warning'
        });
      } finally {
        this.cargandoCliente = false;
      }
    },

    async buscarArticulo(index) {
      const detalle = this.venta.detalles[index];
      if (!detalle.idArticulo) return;

      detalle.cargando = true;
      try {
        console.log('Buscando artículo con ID:', detalle.idArticulo);
        // Primero obtener el artículo para los datos básicos
        const articuloResponse = await this.$api.get(`/articulos/${detalle.idArticulo}`);
        console.log('Respuesta del artículo:', articuloResponse.data);

        if (articuloResponse.data) {
          const articulo = articuloResponse.data;

          // Asignar los datos del artículo directamente
          detalle.nombreProducto = articulo.nombreProducto;
          detalle.talla = articulo.talla;
          detalle.color = articulo.color;
          
          try {
            // Intentar obtener el precio de venta del detalle de venta
            const detalleResponse = await this.$api.get(`/detalles-venta/articulo/${detalle.idArticulo}`);
            console.log('Respuesta del detalle de venta:', detalleResponse.data);

            if (detalleResponse.data && detalleResponse.data.length > 0) {
              detalle.precioUnitario = detalleResponse.data[0].precioUnitario;
              console.log('Precio de venta asignado:', detalle.precioUnitario);
            } else {
              // Si no hay ventas previas, calcular el precio de venta como 50% más que el precio de coste
              detalle.precioUnitario = articulo.precioCoste * 1.5;
              console.log('Usando precio calculado:', detalle.precioUnitario);
            }
          } catch (error) {
            console.warn('No se pudo obtener el precio de venta, usando precio calculado:', error);
            detalle.precioUnitario = articulo.precioCoste * 1.5;
          }
          
          detalle.datosAutomaticos = true;

          // Recalcular el precio total
          this.calcularPrecioTotal(index);
        }
      } catch (error) {
        console.error('Error al buscar artículo:', error);
        console.log('Detalles del error:', error.response ? error.response.data : 'No hay detalles');
        detalle.datosAutomaticos = false;
        this.$q.notify({
          color: 'negative',
          message: 'No se encontró el artículo con ese ID',
          icon: 'warning'
        });
      } finally {
        detalle.cargando = false;
      }
    },

    async procesarVenta(estado) {
      if (this.venta.detalles.length === 0) {
        this.$q.notify({
          color: 'warning',
          message: 'Debe agregar al menos un artículo a la venta.',
          icon: 'warning',
        });
        return;
      }

      // Verificar si los detalles de cada artículo son válidos
      for (let detalle of this.venta.detalles) {
        if (!detalle.idArticulo || !detalle.nombreProducto || !detalle.talla || !detalle.color) {
          this.$q.notify({
            color: 'negative',
            message: 'Faltan datos importantes del artículo. Verifique que el artículo tenga todos los campos completos.',
            icon: 'warning',
          });
          return;
        }
      }

      this.loading = true;
      try {
        console.log(`Procesando venta con estado: ${estado}`, this.venta);

        // Preparar los datos para enviar al backend
        const ventaData = {
          cliente: {
            id: parseInt(this.venta.idCliente)
          },
          almacen: {
            id: parseInt(this.venta.idAlmacen)
          },
          totalSinIva: this.venta.totalSinIVA,
          totalConIva: this.venta.totalConIVA,
          numeroTicket: this.venta.numeroTicket,
          estado: estado, // Usar el estado pasado como parámetro
          fecha: this.venta.fecha,
          detallesVenta: this.venta.detalles.map(detalle => {
            return {
              articulo: {
                id: parseInt(detalle.idArticulo),
                producto: {
                  id: parseInt(detalle.idProducto),
                  nombre: detalle.nombreProducto
                },
                talla: {
                  id: parseInt(detalle.idTalla),
                  talla: detalle.talla 
                },
                color: {
                  id: parseInt(detalle.idColor),
                  color: detalle.color 
                },
                precio: detalle.precioUnitario
              },
              cantidad: detalle.cantidad,
              precioUnitario: detalle.precioUnitario,
              precioSinIva: detalle.precioTotal,
              iva: detalle.precioTotal * 0.21,
              precioTotal: detalle.precioTotal * 1.21
            };
          })
        };

        console.log('Datos preparados para enviar:', ventaData);
        console.log('JSON stringified:', JSON.stringify(ventaData));

        const response = await this.$api.post('/ventas', ventaData);
        console.log('Respuesta del servidor:', response.data);

        // Mensaje según el estado
        const mensaje = estado === 'pendiente' 
          ? 'Venta guardada como pendiente correctamente.' 
          : 'Venta realizada correctamente.';

        this.$q.notify({
          color: 'positive',
          message: mensaje,
          icon: 'check',
        });

        // Reiniciar el formulario
        this.venta = {
          idCliente: null,
          nombreCliente: '',
          fecha: this.obtenerFechaActual(),
          numeroTicket: this.generarNumeroTicket(),
          estado: 'pendiente',
          idAlmacen: 1,
          totalSinIVA: 0,
          totalConIVA: 0,
          detalles: []
        };
      } catch (error) {
        // Error handling
        console.error('Error al procesar la venta:', error);

        if (error.response) {
          console.log('Error data:', error.response.data);
          console.log('Error status:', error.response.status);
          console.log('Error headers:', error.response.headers);
        } else if (error.request) {
          console.log('Error request:', error.request);
        } else {
          console.log('Error message:', error.message);
        }

        let errorMessage = 'Hubo un error al procesar la venta.';

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
    },

    obtenerFechaActual() {
      const today = new Date();
      const year = today.getFullYear();
      const month = String(today.getMonth() + 1).padStart(2, '0');
      const day = String(today.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },

    generarNumeroTicket() {
      const timestamp = new Date().getTime();
      return `TK-${timestamp}`;
    },

    mostrarDialogoNuevoCliente() {
      this.nuevoCliente = {
        nombre: '',
        email: '',
        telefono: ''
      };
      this.dialogoNuevoCliente = true;
    },

    async crearCliente() {
      if (!this.nuevoCliente.nombre || !this.nuevoCliente.email) {
        this.$q.notify({
          color: 'negative',
          message: 'Por favor complete los campos requeridos',
          icon: 'warning'
        });
        return;
      }

      this.creandoCliente = true;
      try {
        const response = await this.$api.post('/clientes', this.nuevoCliente);
        console.log('Cliente creado:', response.data);
        
        // Actualizar el formulario con el nuevo cliente
        this.venta.idCliente = response.data.id;
        this.venta.nombreCliente = response.data.nombre;

        this.$q.notify({
          color: 'positive',
          message: 'Cliente creado correctamente',
          icon: 'check'
        });

        this.dialogoNuevoCliente = false;
      } catch (error) {
        console.error('Error al crear cliente:', error);
        this.$q.notify({
          color: 'negative',
          message: 'Error al crear el cliente',
          icon: 'warning'
        });
      } finally {
        this.creandoCliente = false;
      }
    }
  },
  created() {
    // Establecer la fecha actual
    this.venta.fecha = this.obtenerFechaActual();

    // Generar un número de ticket por defecto basado en la fecha y hora actual
    this.venta.numeroTicket = this.generarNumeroTicket();

    // Cargar los almacenes disponibles con reintentos
    this.cargarAlmacenes();
  }
};
</script>
