<template>
  <q-page padding>
    <!-- Contenido para Agregar Stock -->
    <q-banner class="bg-primary text-white" rounded>
      <div class="text-h6">Agregar Artículos al Stock</div>
    </q-banner>

    <!-- Grid de artículos disponibles -->
    <div class="q-pa-md">
      <q-card class="q-mb-md">
        <q-card-section>
          <div class="row justify-between items-center">
            <div class="text-h6">Artículos Disponibles</div>
            <q-btn 
              color="primary" 
              label="Crear Nuevo Artículo" 
              icon="add" 
              @click="mostrarDialogoNuevoArticulo = true"
            />
          </div>
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
                    <q-btn color="primary" label="Seleccionar" dense flat @click.stop="seleccionarArticulo(props.row)" />
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

      <!-- Formulario para agregar stock -->
      <q-card>
        <q-card-section>
          <div class="text-h6">Agregar Stock</div>
          <q-form @submit.prevent="agregarStock" class="q-mt-md">
            <div class="row q-col-gutter-md">
              <div class="col-12 col-md-4">
                <q-input 
                  v-model="crearStock.idArticulo" 
                  label="ID Artículo" 
                  type="number"
                  outlined
                  required
                  :readonly="!!articuloSeleccionado"
                  :hint="articuloSeleccionado ? `${articuloSeleccionado.nombreProducto} - ${articuloSeleccionado.talla} - ${articuloSeleccionado.color}` : 'Ingrese ID o seleccione un artículo de la tabla'"
                  stack-label
                />
                <div class="q-mt-sm" v-if="!articuloSeleccionado && crearStock.idArticulo">
                  <q-btn 
                    label="Verificar ID" 
                    color="info" 
                    dense 
                    @click="verificarArticulo" 
                    :loading="verificandoArticulo"
                  />
                </div>
              </div>
              <div class="col-12 col-md-4">
                <q-select
                  v-model="crearStock.idAlmacen"
                  :options="almacenes"
                  option-value="id"
                  option-label="nombre"
                  label="Almacén"
                  outlined
                  emit-value
                  map-options
                  required
                  :loading="cargandoAlmacenes"
                />
              </div>
              <div class="col-12 col-md-4">
                <q-input 
                  v-model="crearStock.cantidad" 
                  label="Cantidad" 
                  type="number" 
                  min="1" 
                  outlined
                  required 
                />
              </div>
            </div>
            <div class="row q-mt-md">
              <div class="col-12">
                <q-btn 
                  label="Agregar Stock" 
                  color="primary" 
                  type="submit" 
                  :disable="!crearStock.idArticulo || !crearStock.idAlmacen || !crearStock.cantidad" 
                />
              </div>
            </div>
          </q-form>
        </q-card-section>
      </q-card>
    </div>

    <!-- Diálogo para crear nuevo artículo -->
    <q-dialog v-model="mostrarDialogoNuevoArticulo" persistent>
      <q-card style="min-width: 350px">
        <q-card-section>
          <div class="text-h6">Crear Nuevo Artículo</div>
        </q-card-section>

        <q-card-section>
          <q-form @submit.prevent="crearArticulo">
            <div class="q-gutter-md">
              <q-select
                v-model="nuevoArticulo.idProducto"
                :options="productos"
                option-value="id"
                option-label="nombre"
                label="Producto *"
                outlined
                emit-value
                map-options
                :loading="cargandoProductos"
                :rules="[val => !!val || 'El producto es obligatorio']"
              />

              <q-select
                v-model="nuevoArticulo.idTalla"
                :options="tallas"
                option-value="id"
                option-label="talla"
                label="Talla *"
                outlined
                emit-value
                map-options
                :loading="cargandoTallas"
                :rules="[val => !!val || 'La talla es obligatoria']"
              />

              <q-select
                v-model="nuevoArticulo.idColor"
                :options="colores"
                option-value="id"
                option-label="color"
                label="Color *"
                outlined
                emit-value
                map-options
                :loading="cargandoColores"
                :rules="[val => !!val || 'El color es obligatorio']"
              />

              <q-input
                v-model="nuevoArticulo.precioCoste"
                label="Precio de Coste *"
                type="number"
                step="0.01"
                min="0"
                outlined
                hint="Formato: 0.00 (ej: 19.99)"
                :rules="[
                  val => !!val || 'El precio es obligatorio',
                  val => val >= 0 || 'El precio debe ser mayor o igual a 0'
                ]"
              >
                <template v-slot:prepend>
                  <q-icon name="euro" />
                </template>
              </q-input>
            </div>
          </q-form>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Cancelar" color="primary" v-close-popup />
          <q-btn 
            flat 
            label="Crear" 
            color="positive" 
            @click="crearArticulo" 
            :loading="creandoArticulo"
            :disable="!nuevoArticulo.idProducto || !nuevoArticulo.idTalla || !nuevoArticulo.idColor || this.nuevoArticulo.precioCoste === null"
          />
        </q-card-actions>
      </q-card>
    </q-dialog>
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
      busqueda: '',
      articulos: [],
      almacenes: [],
      productos: [],
      tallas: [],
      colores: [],
      cargandoArticulos: false,
      cargandoAlmacenes: false,
      cargandoProductos: false,
      cargandoTallas: false,
      cargandoColores: false,
      creandoArticulo: false,
      verificandoArticulo: false,
      articuloSeleccionado: null,
      mostrarDialogoNuevoArticulo: false,
      nuevoArticulo: {
        idProducto: null,
        idTalla: null,
        idColor: null,
        precioCoste: null
      },
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
  mounted() {
    this.cargarArticulos();
    this.cargarAlmacenes();
    this.cargarProductos();
    this.cargarTallas();
    this.cargarColores();
  },
  methods: {
    async cargarArticulos() {
      this.cargandoArticulos = true;
      try {
        // Obtener todos los artículos
        const response = await this.$api.get('/articulos');
        this.articulos = response.data.map(articulo => ({
          id: articulo.id,
          nombreProducto: articulo.producto?.nombre || 'Sin nombre',
          talla: articulo.talla?.talla || 'Sin talla',
          color: articulo.color?.color || 'Sin color'
        }));
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

    async cargarProductos() {
      this.cargandoProductos = true;
      try {
        const response = await this.$api.get('/productos');
        this.productos = response.data;
      } catch (error) {
        console.error('Error al cargar productos:', error);
        this.$q.notify({
          color: 'negative',
          message: 'Error al cargar los productos',
          icon: 'error'
        });
      } finally {
        this.cargandoProductos = false;
      }
    },

    async cargarTallas() {
      this.cargandoTallas = true;
      try {
        const response = await this.$api.get('/tallas');
        this.tallas = response.data;
      } catch (error) {
        console.error('Error al cargar tallas:', error);
        this.$q.notify({
          color: 'negative',
          message: 'Error al cargar las tallas',
          icon: 'error'
        });
      } finally {
        this.cargandoTallas = false;
      }
    },

    async cargarColores() {
      this.cargandoColores = true;
      try {
        const response = await this.$api.get('/colores');
        this.colores = response.data;
      } catch (error) {
        console.error('Error al cargar colores:', error);
        this.$q.notify({
          color: 'negative',
          message: 'Error al cargar los colores',
          icon: 'error'
        });
      } finally {
        this.cargandoColores = false;
      }
    },
    
    seleccionarArticulo(articulo) {
      this.articuloSeleccionado = articulo;
      this.crearStock.idArticulo = articulo.id;
    },
    
    async crearArticulo() {
      if (!this.nuevoArticulo.idProducto || !this.nuevoArticulo.idTalla || !this.nuevoArticulo.idColor || this.nuevoArticulo.precioCoste === null) {
        this.$q.notify({
          color: 'warning',
          message: 'Todos los campos son obligatorios',
          icon: 'warning'
        });
        return;
      }

      this.creandoArticulo = true;
      try {
        // Verificar si ya existe un artículo con la misma combinación
        const combinacionExistente = this.articulos.find(
          a => a.nombreProducto === this.productos.find(p => p.id === this.nuevoArticulo.idProducto)?.nombre &&
               a.talla === this.tallas.find(t => t.id === this.nuevoArticulo.idTalla)?.talla &&
               a.color === this.colores.find(c => c.id === this.nuevoArticulo.idColor)?.color
        );

        if (combinacionExistente) {
          this.$q.notify({
            color: 'negative',
            message: `Ya existe un artículo con esta combinación. ID: ${combinacionExistente.id}`,
            icon: 'error',
            timeout: 5000
          });
          this.creandoArticulo = false;
          return;
        }

        // Asegurarse de que el precio se envía como número
        const precioCoste = parseFloat(this.nuevoArticulo.precioCoste);
        
        // Verificar que el precio es un número válido
        if (isNaN(precioCoste)) {
          this.$q.notify({
            color: 'negative',
            message: 'El precio debe ser un número válido',
            icon: 'error'
          });
          this.creandoArticulo = false;
          return;
        }

        const articuloData = {
          producto: { id: this.nuevoArticulo.idProducto },
          talla: { id: this.nuevoArticulo.idTalla },
          color: { id: this.nuevoArticulo.idColor },
          precio: precioCoste
        };

        console.log('Enviando datos de artículo:', articuloData);

        const response = await this.$api.post('/articulos', articuloData);
        const nuevoArticulo = response.data;

        console.log('Respuesta del servidor:', nuevoArticulo);

        this.$q.notify({
          color: 'positive',
          message: `Artículo creado correctamente. ID: ${nuevoArticulo.id}, Precio: ${nuevoArticulo.precioCoste || nuevoArticulo.precio || 'No disponible'}`,
          icon: 'check'
        });

        // Recargar la lista de artículos
        await this.cargarArticulos();

        // Cerrar el diálogo y limpiar el formulario
        this.mostrarDialogoNuevoArticulo = false;
        this.nuevoArticulo = {
          idProducto: null,
          idTalla: null,
          idColor: null,
          precioCoste: null
        };
        
        // Seleccionar automáticamente el nuevo artículo
        if (nuevoArticulo && nuevoArticulo.id) {
          const articuloEncontrado = this.articulos.find(a => a.id === nuevoArticulo.id);
          if (articuloEncontrado) {
            this.seleccionarArticulo(articuloEncontrado);
            // Desplazar la vista hacia el artículo recién creado
            this.$nextTick(() => {
              const element = document.querySelector(`.q-table__grid-content tr[data-id="${nuevoArticulo.id}"]`);
              if (element) element.scrollIntoView({ behavior: 'smooth', block: 'center' });
            });
          }
        }
      } catch (error) {
        console.error('Error al crear artículo:', error);
        this.$q.notify({
          color: 'negative',
          message: 'Error al crear el artículo',
          icon: 'error'
        });
      } finally {
        this.creandoArticulo = false;
      }
    },
    
    async verificarArticulo() {
      if (!this.crearStock.idArticulo) {
        this.$q.notify({
          color: 'warning',
          message: 'Debe ingresar un ID de artículo',
          icon: 'warning'
        });
        return;
      }
      
      this.verificandoArticulo = true;
      try {
        const response = await this.$api.get(`/articulos/${this.crearStock.idArticulo}`);
        const articulo = response.data;
        
        if (articulo) {
          // Crear un objeto con el formato que espera articuloSeleccionado
          this.articuloSeleccionado = {
            id: articulo.id,
            nombreProducto: articulo.producto?.nombre || 'Sin nombre',
            talla: articulo.talla?.talla || 'Sin talla',
            color: articulo.color?.color || 'Sin color'
          };
          
          this.$q.notify({
            color: 'positive',
            message: `Artículo encontrado: ${this.articuloSeleccionado.nombreProducto} - ${this.articuloSeleccionado.talla} - ${this.articuloSeleccionado.color}`,
            icon: 'check'
          });
        }
      } catch (error) {
        console.error('Error al verificar el artículo:', error);
        this.articuloSeleccionado = null;
        this.$q.notify({
          color: 'negative',
          message: `El artículo con ID ${this.crearStock.idArticulo} no se encontró`,
          icon: 'error'
        });
      } finally {
        this.verificandoArticulo = false;
      }
    },
    
    async agregarStock() {
      if (!this.crearStock.idArticulo) {
        this.$q.notify({
          color: 'warning',
          message: 'Debe seleccionar o ingresar un ID de artículo',
          icon: 'warning'
        });
        return;
      }
      
      // Si tenemos un ID pero no tenemos el artículo seleccionado, verificamos primero
      if (!this.articuloSeleccionado && this.crearStock.idArticulo) {
        await this.verificarArticulo();
        if (!this.articuloSeleccionado) {
          // Si después de verificar sigue sin haber artículo, salimos
          return;
        }
      }
      
      // Aseguramos que los valores sean números
      const datosEnviar = {
        idArticulo: Number(this.crearStock.idArticulo),
        idAlmacen: Number(this.crearStock.idAlmacen),
        cantidad: Number(this.crearStock.cantidad),
      };

      try {
        // Enviamos el StockDTO a la API
        await this.$api.post('/stocks', datosEnviar);

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
        this.articuloSeleccionado = null;
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

<style scoped>
.q-table__card {
  width: 100%;
}
</style>