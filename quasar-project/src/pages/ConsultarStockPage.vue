<template>
  <q-page padding>
    <q-banner class="bg-secondary text-white" rounded>
      <div class="text-h6">Consultar Stock</div>
    </q-banner>

    <!-- Grid de artículos disponibles -->
    <div class="q-pa-md">
      <q-card class="q-mb-md">
        <q-card-section>
          <div class="text-h6">Seleccionar Artículo</div>
          <div class="q-mt-sm">
            <q-input v-model="busqueda" label="Buscar artículo" outlined dense clearable class="q-mb-md">
              <template v-slot:append>
                <q-icon name="search" />
              </template>
            </q-input>
            <q-table
              :rows="articulos"
              :columns="columns"
              row-key="id"
              :loading="cargandoArticulos"
              :filter="busqueda"
              v-model:pagination="pagination"
              @row-click="seleccionarArticulo"
            >
              <template v-slot:body="props">
                <q-tr :props="props" :class="props.row.id === articuloSeleccionado?.id ? 'bg-blue-1' : ''">
                  <q-td key="id" :props="props">{{ props.row.id }}</q-td>
                  <q-td key="nombreProducto" :props="props">{{ props.row.nombreProducto }}</q-td>
                  <q-td key="talla" :props="props">{{ props.row.talla }}</q-td>
                  <q-td key="color" :props="props">{{ props.row.color }}</q-td>
                  <q-td key="acciones" :props="props">
                    <q-btn color="secondary" label="Consultar" dense flat @click.stop="seleccionarYConsultar(props.row)" />
                  </q-td>
                </q-tr>
              </template>
              <template v-slot:no-data>
                <div class="full-width row flex-center q-gutter-sm">
                  <q-icon size="2em" name="sentiment_dissatisfied" />
                  <span>No se encontraron artículos</span>
                </div>
              </template>
            </q-table>
          </div>
        </q-card-section>
      </q-card>

      <!-- Formulario para consultar stock manualmente -->
      <q-card class="q-mb-md">
        <q-card-section>
          <div class="text-h6">Consultar Stock por ID Artículo</div>
          <q-form @submit.prevent="consultarStock" class="q-mt-md">
            <div class="row q-col-gutter-md">
              <div class="col-12 col-md-8">
                <q-input 
                  v-model="idArticuloConsulta" 
                  label="ID Artículo para consultar" 
                  type="number" 
                  outlined
                  required 
                />
              </div>
              <div class="col-12 col-md-4 flex items-center">
                <q-btn 
                  label="Consultar Stock" 
                  color="secondary" 
                  type="submit" 
                  :disable="!idArticuloConsulta" 
                />
              </div>
            </div>
          </q-form>
        </q-card-section>
      </q-card>

      <!-- Mostrar resultados de la consulta -->
      <q-card v-if="stockConsulta.length > 0" class="q-mb-md">
        <q-card-section>
          <div class="text-h6">
            Stock del artículo con ID: {{ idArticuloConsulta }} 
            <span v-if="articuloSeleccionado">
              ({{ articuloSeleccionado.nombreProducto }}, {{ articuloSeleccionado.color }}, {{ articuloSeleccionado.talla }})
            </span>
          </div>
          <q-list bordered separator>
            <q-item v-for="(stock, index) in stockConsultaConNombres" :key="index">
              <q-item-section>
                <q-item-label>
                  <strong>Almacén:</strong> {{ stock.nombreAlmacen }}
                </q-item-label>
                <q-item-label caption>
                  <strong>Cantidad:</strong> {{ stock.cantidad }}
                </q-item-label>
              </q-item-section>
            </q-item>
          </q-list>
        </q-card-section>
      </q-card>
      
      <div v-else-if="consultaRealizada && stockConsulta.length === 0" class="q-pa-md text-center">
        <q-icon name="inventory_2" size="3rem" color="grey-7" />
        <p class="text-grey-7">No se encontró stock para este artículo en ningún almacén.</p>
      </div>
    </div>
  </q-page>
</template>

<script>
export default {
  data() {
    return {
      idArticuloConsulta: null,
      stockConsulta: [], // Almacena los datos consultados
      consultaRealizada: false,
      busqueda: '',
      articulos: [],
      almacenes: [],
      cargandoArticulos: false,
      cargandoAlmacenes: false,
      articuloSeleccionado: null,
      pagination: {
        rowsPerPage: 10
      },
      columns: [
        { name: 'id', label: 'ID', field: 'id', sortable: true, align: 'left' },
        { name: 'nombreProducto', label: 'Producto', field: 'nombreProducto', sortable: true, align: 'left' },
        { name: 'talla', label: 'Talla', field: 'talla', sortable: true, align: 'center' },
        { name: 'color', label: 'Color', field: 'color', sortable: true, align: 'center' },
        { name: 'acciones', label: 'Acciones', field: 'acciones', align: 'center' }
      ]
    };
  },
  computed: {
    stockConsultaConNombres() {
      return this.stockConsulta.map(stock => {
        const almacen = this.almacenes.find(a => a.id === stock.idAlmacen);
        return {
          ...stock,
          nombreAlmacen: almacen ? almacen.nombre : `Almacén ID: ${stock.idAlmacen}`
        };
      });
    }
  },
  mounted() {
    this.cargarArticulos();
    this.cargarAlmacenes();
  },
  methods: {
    async cargarArticulos() {
      this.cargandoArticulos = true;
      try {
        // Obtener todos los artículos
        const response = await this.$api.get('/articulos');
        console.log('Respuesta de artículos:', response.data);
        this.articulos = response.data.map(articulo => ({
          id: articulo.id,
          nombreProducto: articulo.nombreProducto,
          talla: articulo.talla,
          color: articulo.color,
          precioCoste: articulo.precioCoste,
          precioVenta: articulo.precioVenta
        }));
        console.log('Artículos mapeados:', this.articulos);
      } catch (error) {
        console.error('Error al cargar artículos:', error);
        this.$q.notify({
          color: 'negative',
          message: 'Error al cargar los artículos',
          icon: 'error'
        });
      } finally {
        this.cargandoArticulos = false;
      }
    },
    
    async cargarAlmacenes() {
      this.cargandoAlmacenes = true;
      try {
        const response = await this.$api.get('/almacenes');
        this.almacenes = response.data;
      } catch (error) {
        console.error('Error al cargar almacenes:', error);
        this.$q.notify({
          color: 'negative',
          message: 'Error al cargar los almacenes',
          icon: 'error'
        });
      } finally {
        this.cargandoAlmacenes = false;
      }
    },
    
    seleccionarArticulo(articulo) {
      this.articuloSeleccionado = articulo;
      this.idArticuloConsulta = articulo.id;
    },
    
    seleccionarYConsultar(articulo) {
      this.seleccionarArticulo(articulo);
      this.consultarStock();
    },
    
    async consultarStock() {
      if (!this.idArticuloConsulta) {
        this.$q.notify({
          color: 'warning',
          message: 'Debe ingresar un ID de artículo',
          icon: 'warning'
        });
        return;
      }
      
      try {
        console.log('Consultando stock para artículo ID:', this.idArticuloConsulta);
        const response = await this.$api.get(`/stocks/articulo/${this.idArticuloConsulta}`);
        console.log('Respuesta del servidor:', response.data);
        this.stockConsulta = response.data;
        this.consultaRealizada = true;
        
        // Si no hay artículo seleccionado pero tenemos ID, buscamos el artículo
        if (!this.articuloSeleccionado && this.idArticuloConsulta) {
          const articuloEncontrado = this.articulos.find(a => a.id === Number(this.idArticuloConsulta));
          if (articuloEncontrado) {
            this.articuloSeleccionado = articuloEncontrado;
          }
        }
        
        if (this.stockConsulta.length === 0) {
          this.$q.notify({
            color: 'info',
            message: 'No se encontró stock para este artículo en ningún almacén.',
            icon: 'info'
          });
        }
      } catch (error) {
        console.error('Error al consultar el stock:', error);
        console.log('Detalles del error:', error.response ? error.response.data : 'No hay detalles');
        this.$q.notify({
          color: 'negative',
          message: 'Hubo un error al consultar el stock.',
          icon: 'warning',
        });
        this.stockConsulta = [];
        this.consultaRealizada = true;
      }
    }
  }
};
</script>

<style scoped>
.q-table__card {
  width: 100%;
}
</style>
