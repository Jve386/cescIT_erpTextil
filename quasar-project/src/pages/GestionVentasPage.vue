<template>
  <q-page padding>
    <q-banner class="bg-primary text-white" rounded>
      <div class="text-h6">Gestión de Ventas</div>
    </q-banner>

    <!-- Búsqueda de ventas -->
    <div class="q-pa-md">
      <div class="row q-col-gutter-md">
        <div class="col-12 col-md-6">
          <q-input 
            v-model="busqueda" 
            label="Buscar por número de ticket" 
            outlined 
            clearable
            @keyup.enter="buscarVentas"
          >
            <template v-slot:append>
              <q-icon name="search" @click="buscarVentas" class="cursor-pointer" />
            </template>
          </q-input>
        </div>
        <div class="col-12 col-md-6">
          <q-select
            v-model="filtroEstado"
            :options="opcionesEstado"
            label="Filtrar por estado"
            outlined
            emit-value
            map-options
            @update:model-value="buscarVentas"
          />
        </div>
      </div>
    </div>

    <!-- Tabla de ventas -->
    <div class="q-pa-md">
      <q-table
        title="Ventas"
        :rows="ventas"
        :columns="columns"
        row-key="id"
        :loading="cargando"
        :filter="busqueda"
        v-model:pagination="pagination"
      >
        <template v-slot:body="props">
          <q-tr :props="props">
            <q-td key="id" :props="props">{{ props.row.id }}</q-td>
            <q-td key="numeroTicket" :props="props">{{ props.row.numeroTicket }}</q-td>
            <q-td key="fecha" :props="props">{{ formatearFecha(props.row.fecha) }}</q-td>
            <q-td key="nombreCliente" :props="props">{{ props.row.nombreCliente }}</q-td>
            <q-td key="totalConIVA" :props="props">{{ formatearPrecio(props.row.totalConIVA) }}</q-td>
            <q-td key="estado" :props="props">
              <q-badge :color="props.row.estado === 'pendiente' ? 'warning' : 'positive'">
                {{ props.row.estado }}
              </q-badge>
            </q-td>
            <q-td key="acciones" :props="props">
              <div class="q-gutter-sm">
                <q-btn 
                  v-if="props.row.estado === 'pendiente'" 
                  size="sm" 
                  color="positive" 
                  icon="check_circle" 
                  @click="confirmarCompletarVenta(props.row)"
                  flat
                  dense
                  :disable="cargando"
                >
                  <q-tooltip>Completar venta</q-tooltip>
                </q-btn>
                <q-btn 
                  size="sm" 
                  color="primary" 
                  icon="visibility" 
                  @click="verDetallesVenta(props.row)"
                  flat
                  dense
                  :disable="cargando"
                >
                  <q-tooltip>Ver detalles</q-tooltip>
                </q-btn>
                <q-btn 
                  size="sm" 
                  color="negative" 
                  icon="delete" 
                  @click="confirmarEliminarVenta(props.row)"
                  flat
                  dense
                  :disable="cargando"
                >
                  <q-tooltip>Eliminar venta</q-tooltip>
                </q-btn>
              </div>
            </q-td>
          </q-tr>
        </template>

        <template v-slot:no-data>
          <div class="full-width row flex-center q-gutter-sm">
            <q-icon size="2em" name="sentiment_dissatisfied" />
            <span>No se encontraron ventas</span>
          </div>
        </template>
      </q-table>
    </div>

    <!-- Diálogo para ver detalles de venta -->
    <q-dialog v-model="dialogoDetalles" persistent max-width="1000px">
      <q-card style="min-width: 350px">
        <q-card-section class="row items-center">
          <div class="text-h6">Detalles de la Venta</div>
          <q-space />
          <q-btn icon="close" flat round dense v-close-popup />
        </q-card-section>

        <q-card-section>
          <div class="row q-col-gutter-md">
            <div class="col-12 col-md-6">
              <q-item>
                <q-item-section>
                  <q-item-label caption>Número de Ticket</q-item-label>
                  <q-item-label>{{ ventaSeleccionada.numeroTicket }}</q-item-label>
                </q-item-section>
              </q-item>
            </div>
            <div class="col-12 col-md-6">
              <q-item>
                <q-item-section>
                  <q-item-label caption>Fecha</q-item-label>
                  <q-item-label>{{ formatearFecha(ventaSeleccionada.fecha) }}</q-item-label>
                </q-item-section>
              </q-item>
            </div>
            <div class="col-12 col-md-6">
              <q-item>
                <q-item-section>
                  <q-item-label caption>Cliente</q-item-label>
                  <q-item-label>{{ ventaSeleccionada.nombreCliente }}</q-item-label>
                </q-item-section>
              </q-item>
            </div>
            <div class="col-12 col-md-6">
              <q-item>
                <q-item-section>
                  <q-item-label caption>Estado</q-item-label>
                  <q-item-label>
                    <q-badge :color="ventaSeleccionada.estado === 'pendiente' ? 'warning' : 'positive'">
                      {{ ventaSeleccionada.estado }}
                    </q-badge>
                  </q-item-label>
                </q-item-section>
              </q-item>
            </div>
          </div>

          <q-separator class="q-my-md" />

          <div class="text-subtitle1 q-mb-sm">Artículos</div>
          <q-table
            :rows="ventaSeleccionada.detalles || []"
            :columns="columnsDetalles"
            row-key="id"
            dense
            hide-pagination
            :pagination="{rowsPerPage: 0}"
          >
            <template v-slot:body="props">
              <q-tr :props="props">
                <q-td key="nombreProducto" :props="props">{{ props.row.nombreProducto }}</q-td>
                <q-td key="talla" :props="props">{{ props.row.talla }}</q-td>
                <q-td key="color" :props="props">{{ props.row.color }}</q-td>
                <q-td key="cantidad" :props="props">{{ props.row.cantidad }}</q-td>
                <q-td key="precioUnitario" :props="props">{{ formatearPrecio(props.row.precioUnitario) }}</q-td>
                <q-td key="precioTotal" :props="props">{{ formatearPrecio(props.row.precioTotal) }}</q-td>
              </q-tr>
            </template>
          </q-table>

          <div class="row justify-end q-mt-md">
            <div class="col-12 col-md-4">
              <q-item>
                <q-item-section>
                  <q-item-label caption>Total sin IVA</q-item-label>
                  <q-item-label class="text-weight-bold">{{ formatearPrecio(ventaSeleccionada.totalSinIVA) }}</q-item-label>
                </q-item-section>
              </q-item>
            </div>
            <div class="col-12 col-md-4">
              <q-item>
                <q-item-section>
                  <q-item-label caption>Total con IVA</q-item-label>
                  <q-item-label class="text-weight-bold">{{ formatearPrecio(ventaSeleccionada.totalConIVA) }}</q-item-label>
                </q-item-section>
              </q-item>
            </div>
          </div>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Cerrar" color="primary" v-close-popup />
          <q-btn 
            v-if="ventaSeleccionada.estado === 'pendiente'" 
            flat 
            label="Completar Venta" 
            color="positive" 
            @click="completarVentaDesdeDialogo"
          />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <!-- Diálogo de confirmación para completar venta -->
    <q-dialog v-model="dialogoCompletarVenta" persistent>
      <q-card>
        <q-card-section class="row items-center">
          <q-avatar icon="check_circle" color="positive" text-color="white" />
          <span class="q-ml-sm">¿Está seguro que desea completar esta venta?</span>
        </q-card-section>
        <q-card-section>
          <p>Al completar la venta, se actualizará el stock de los artículos.</p>
          <p>Ticket: <strong>{{ ventaSeleccionada.numeroTicket }}</strong></p>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn flat label="Cancelar" color="primary" v-close-popup />
          <q-btn flat label="Completar" color="positive" @click="completarVenta" :loading="cargandoAccion" />
        </q-card-actions>
      </q-card>
    </q-dialog>

    <!-- Diálogo de confirmación para eliminar venta -->
    <q-dialog v-model="dialogoEliminarVenta" persistent>
      <q-card>
        <q-card-section class="row items-center">
          <q-avatar icon="delete" color="negative" text-color="white" />
          <span class="q-ml-sm">¿Está seguro que desea eliminar esta venta?</span>
        </q-card-section>
        <q-card-section>
          <p>Esta acción no se puede deshacer.</p>
          <p>Ticket: <strong>{{ ventaSeleccionada.numeroTicket }}</strong></p>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn flat label="Cancelar" color="primary" v-close-popup />
          <q-btn flat label="Eliminar" color="negative" @click="eliminarVenta" :loading="cargandoAccion" />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </q-page>
</template>

<script>
export default {
  data() {
    return {
      busqueda: '',
      filtroEstado: null,
      opcionesEstado: [
        { label: 'Todos', value: null },
        { label: 'Pendientes', value: 'pendiente' },
        { label: 'Completadas', value: 'completada' }
      ],
      ventas: [],
      cargando: false,
      cargandoAccion: false,
      dialogoDetalles: false,
      dialogoCompletarVenta: false,
      dialogoEliminarVenta: false,
      ventaSeleccionada: {},
      pagination: {
        rowsPerPage: 10
      },
      columns: [
        { name: 'id', label: 'ID', field: 'id', sortable: true, align: 'left' },
        { name: 'numeroTicket', label: 'Número de Ticket', field: 'numeroTicket', sortable: true, align: 'left' },
        { name: 'fecha', label: 'Fecha', field: 'fecha', sortable: true, align: 'left' },
        { name: 'nombreCliente', label: 'Cliente', field: 'nombreCliente', sortable: true, align: 'left' },
        { name: 'totalConIVA', label: 'Total', field: 'totalConIVA', sortable: true, align: 'right' },
        { name: 'estado', label: 'Estado', field: 'estado', sortable: true, align: 'center' },
        { name: 'acciones', label: 'Acciones', field: 'acciones', align: 'center' }
      ],
      columnsDetalles: [
        { name: 'nombreProducto', label: 'Producto', field: 'nombreProducto', align: 'left' },
        { name: 'talla', label: 'Talla', field: 'talla', align: 'center' },
        { name: 'color', label: 'Color', field: 'color', align: 'center' },
        { name: 'cantidad', label: 'Cantidad', field: 'cantidad', align: 'center' },
        { name: 'precioUnitario', label: 'Precio Unit.', field: 'precioUnitario', align: 'right' },
        { name: 'precioTotal', label: 'Total', field: 'precioTotal', align: 'right' }
      ]
    };
  },
  methods: {
    async cargarVentas() {
      this.cargando = true;
      try {
        const response = await this.$api.get('/ventas');
        this.ventas = response.data;
        
        // Aplicar filtro de estado si está seleccionado
        if (this.filtroEstado) {
          this.ventas = this.ventas.filter(venta => venta.estado === this.filtroEstado);
        }
      } catch (error) {
        console.error('Error al cargar ventas:', error);
        this.$q.notify({
          color: 'negative',
          message: 'Error al cargar las ventas',
          icon: 'error'
        });
      } finally {
        this.cargando = false;
      }
    },
    
    async buscarVentas() {
      this.cargando = true;
      try {
        let url = '/ventas';
        
        // Si hay búsqueda por número de ticket, filtrar en el frontend
        const response = await this.$api.get(url);
        let ventas = response.data;
        
        // Aplicar filtro de estado si está seleccionado
        if (this.filtroEstado) {
          ventas = ventas.filter(venta => venta.estado === this.filtroEstado);
        }
        
        // Aplicar filtro de búsqueda por número de ticket
        if (this.busqueda) {
          ventas = ventas.filter(venta => 
            venta.numeroTicket.toLowerCase().includes(this.busqueda.toLowerCase())
          );
        }
        
        this.ventas = ventas;
      } catch (error) {
        console.error('Error al buscar ventas:', error);
        this.$q.notify({
          color: 'negative',
          message: 'Error al buscar ventas',
          icon: 'error'
        });
      } finally {
        this.cargando = false;
      }
    },
    
    verDetallesVenta(venta) {
      this.ventaSeleccionada = { ...venta };
      this.dialogoDetalles = true;
    },
    
    confirmarCompletarVenta(venta) {
      this.ventaSeleccionada = { ...venta };
      this.dialogoCompletarVenta = true;
    },
    
    confirmarEliminarVenta(venta) {
      this.ventaSeleccionada = { ...venta };
      this.dialogoEliminarVenta = true;
    },
    
    async completarVenta() {
      this.cargandoAccion = true;
      try {
        await this.$api.put(`/ventas/${this.ventaSeleccionada.id}/completar`);
        
        this.$q.notify({
          color: 'positive',
          message: 'Venta completada correctamente',
          icon: 'check_circle'
        });
        
        // Actualizar la lista de ventas
        await this.cargarVentas();
        
        // Cerrar el diálogo
        this.dialogoCompletarVenta = false;
      } catch (error) {
        console.error('Error al completar la venta:', error);
        
        let errorMessage = 'Error al completar la venta';
        if (error.response && error.response.data) {
          errorMessage += ': ' + (error.response.data.message || JSON.stringify(error.response.data));
        }
        
        this.$q.notify({
          color: 'negative',
          message: errorMessage,
          icon: 'error'
        });
      } finally {
        this.cargandoAccion = false;
      }
    },
    
    completarVentaDesdeDialogo() {
      this.dialogoDetalles = false;
      this.dialogoCompletarVenta = true;
    },
    
    async eliminarVenta() {
      this.cargandoAccion = true;
      try {
        await this.$api.delete(`/ventas/${this.ventaSeleccionada.id}`);
        
        this.$q.notify({
          color: 'positive',
          message: 'Venta eliminada correctamente',
          icon: 'check_circle'
        });
        
        // Actualizar la lista de ventas
        await this.cargarVentas();
        
        // Cerrar el diálogo
        this.dialogoEliminarVenta = false;
      } catch (error) {
        console.error('Error al eliminar la venta:', error);
        
        let errorMessage = 'Error al eliminar la venta';
        if (error.response && error.response.data) {
          errorMessage += ': ' + (error.response.data.message || JSON.stringify(error.response.data));
        }
        
        this.$q.notify({
          color: 'negative',
          message: errorMessage,
          icon: 'error'
        });
      } finally {
        this.cargandoAccion = false;
      }
    },
    
    formatearFecha(fecha) {
      if (!fecha) return '';
      
      // Si la fecha es un string ISO, convertirla a objeto Date
      const date = typeof fecha === 'string' ? new Date(fecha) : fecha;
      
      return date.toLocaleDateString('es-ES', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
      });
    },
    
    formatearPrecio(precio) {
      if (precio === null || precio === undefined) return '';
      
      return new Intl.NumberFormat('es-ES', {
        style: 'currency',
        currency: 'EUR'
      }).format(precio);
    }
  },
  created() {
    this.cargarVentas();
  }
};
</script>

<style scoped>
.q-table__card {
  width: 100%;
}
</style> 