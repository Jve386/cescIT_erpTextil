<template>
    <q-page>
        <q-banner class="bg-primary text-white" rounded>
            <div class="text-h6">Lista de Artículos</div>
        </q-banner>

        <q-btn label="Cargar Artículos" @click="getArticulos" color="primary" class="q-mb-md" />

        <q-list bordered class="q-pa-md">
            <q-item-label v-if="loading" class="text-center">Cargando...</q-item-label>

            <q-item v-for="articulo in articulos" :key="articulo.id">
                <q-item-section>
                    <!-- Mostrar nombre y descripción del producto -->
                    <q-item-label class="text-h6">{{ articulo.producto.nombre }}</q-item-label>
                    <q-item-label>{{ articulo.producto.descripcion }}</q-item-label>
                    <!-- Mostrar talla y color -->
                    <q-item-label>Talla: {{ articulo.talla.talla }}</q-item-label>
                    <q-item-label>Color: {{ articulo.color.color }}</q-item-label>
                    <!-- Mostrar el precio con el método formatCurrency -->
                    <q-item-label>{{ formatCurrency(articulo.precio) }}</q-item-label>
                </q-item-section>
            </q-item>

            <q-item v-if="!articulos.length && !loading">
                <q-item-section>
                    <q-item-label>No hay artículos disponibles.</q-item-label>
                </q-item-section>
            </q-item>
        </q-list>
    </q-page>
</template>

<script>
export default {
    data() {
        return {
            articulos: [],
            loading: false,
        };
    },
    methods: {
        // Método para formatear el precio
        formatCurrency(value) {
            return new Intl.NumberFormat('es-ES', {
                style: 'currency',
                currency: 'EUR'
            }).format(value);
        },

        // ARTICULOS
        async getArticulos() {
            this.loading = true;
            try {
                // Solicitud GET a la API
                const response = await this.$api.get('/articulos');
                
                // Asigna los datos recibidos a la variable articulos
                this.articulos = response.data;
            } catch (error) {
                console.error('Error al cargar los artículos:', error);
                this.$q.notify({
                    color: 'negative',
                    message: 'Hubo un error al cargar los artículos.',
                    icon: 'warning',
                });
            } finally {
                this.loading = false;
            }
        },
    },
    mounted() {
        this.getArticulos();
    },
};
</script>