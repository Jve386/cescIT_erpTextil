<template>
    <q-page padding>
        <q-banner class="bg-primary text-white" rounded>
            <div class="text-h6">Estadísticas</div>
        </q-banner>

        <div class="q-pa-md">
            <div class="row q-col-gutter-md">
                <!-- Stock por Almacén -->
                <div class="col-12 col-md-6">
                    <q-card>
                        <q-card-section>
                            <div class="text-h6">Stock por Almacén</div>
                            <div class="chart-container" style="position: relative; height: 400px;">
                                <canvas ref="stockChart"></canvas>
                            </div>
                        </q-card-section>
                    </q-card>
                </div>

                <!-- Stock por Producto -->
                <div class="col-12 col-md-6">
                    <q-card>
                        <q-card-section>
                            <div class="text-h6">Stock por Articulo (%)</div>
                            <div class="chart-container" style="position: relative; height: 400px;">
                                <canvas ref="productoChart"></canvas>
                            </div>
                        </q-card-section>
                    </q-card>
                </div>
                <!-- Ventas Diarias -->
                <div class="col-12">
                    <q-card>
                        <q-card-section>
                            <div class="text-h6">Ventas Diarias</div>
                            <div class="chart-container" style="position: relative; height: 400px;">
                                <canvas ref="ventasChart"></canvas>
                            </div>
                        </q-card-section>
                    </q-card>
                </div>
            </div>
        </div>
    </q-page>
</template>

<script>
import { Chart, registerables } from 'chart.js';
Chart.register(...registerables);

export default {
    name: 'EstadisticasPage',
    data() {
        return {
            stockChart: null,
            productoChart: null,
            cargando: false,
            datosStock: [],
            datosProductos: [],
            almacenes: [],
            articulos: [],
            datosVentas: [],
        };
    },
    methods: {
        async cargarDatos() {
            this.cargando = true;
            try {
                // cargar los almacenes para obtener sus nombres
                await this.cargarAlmacenes();

                //cargar los artículos para obtener sus nombres
                await this.cargarArticulos();

                // cargar los datos de stock
                await this.cargarStocks();

                // cargar los datos de ventas
                await this.cargarVentas();

                // Crear gráficos
                this.crearGraficoStock();
                this.crearGraficoProductos();
                this.crearGraficoVentas();
            } catch (error) {
                console.error('Error al cargar estadísticas:', error);
                this.$q.notify({
                    color: 'negative',
                    message: 'Error al cargar las estadísticas',
                    icon: 'error'
                });
            } finally {
                this.cargando = false;
            }
        },

        async cargarAlmacenes() {
            try {
                const response = await this.$api.get('/almacenes');
                this.almacenes = response.data;
                console.log('Almacenes cargados:', this.almacenes);
            } catch (error) {
                console.error('Error al cargar almacenes:', error);
                throw error;
            }
        },

        async cargarArticulos() {
            try {
                const response = await this.$api.get('/articulos');
                this.articulos = response.data;
                console.log('Artículos cargados:', this.articulos);
            } catch (error) {
                console.error('Error al cargar artículos:', error);
                throw error;
            }
        },

        async cargarStocks() {
            try {
                // Obtener datos de stock desde la API
                const response = await this.$api.get('/stocks');
                const stocks = response.data;
                console.log('Stocks cargados:', stocks);

                // Procesar datos para Stock por Almacén
                // Agrupar stocks por almacén y calcular total
                const stockPorAlmacen = {};

                stocks.forEach(stock => {
                    if (!stockPorAlmacen[stock.idAlmacen]) {
                        stockPorAlmacen[stock.idAlmacen] = 0;
                    }
                    stockPorAlmacen[stock.idAlmacen] += stock.cantidad;
                });

                this.datosStock = Object.keys(stockPorAlmacen).map(idAlmacen => {
                    const almacen = this.almacenes.find(a => a.id === parseInt(idAlmacen));
                    return {
                        idAlmacen: parseInt(idAlmacen),
                        nombreAlmacen: almacen ? almacen.nombre : `Almacén ID: ${idAlmacen}`,
                        cantidadTotal: stockPorAlmacen[idAlmacen]
                    };
                });

                // Procesar datos para Stock por Producto (porcentaje)
                // Agrupar stocks por producto y calcular total
                const stockPorProducto = {};

                stocks.forEach(stock => {
                    // Obtener el artículo correspondiente para encontrar el idProducto
                    const articulo = this.articulos.find(a => a.id === stock.idArticulo);
                    if (articulo) {
                        const idProducto = articulo.id;
                        if (!stockPorProducto[idProducto]) {
                            stockPorProducto[idProducto] = {
                                cantidad: 0,
                                nombreProducto: articulo.nombreProducto,
                                color: articulo.color,
                                talla: articulo.talla
                            };
                        }
                        stockPorProducto[idProducto].cantidad += stock.cantidad;
                    }
                });

                const totalStock = Object.values(stockPorProducto).reduce((acc, item) => acc + item.cantidad, 0);

                this.datosProductos = Object.keys(stockPorProducto).map(idProducto => {
                    const producto = stockPorProducto[idProducto];
                    return {
                        idProducto,
                        nombreProducto: `${producto.nombreProducto} (${producto.color}, ${producto.talla})`,
                        cantidad: producto.cantidad,
                        porcentaje: ((producto.cantidad / totalStock) * 100).toFixed(2)
                    };
                });

                console.log('Datos procesados - Stock por Almacén:', this.datosStock);
                console.log('Datos procesados - Stock por Producto:', this.datosProductos);
            } catch (error) {
                console.error('Error al cargar stocks:', error);
                throw error;
            }
        },

        async cargarVentas() {
            try {
                const response = await this.$api.get('/ventas');
                const ventasData = response.data;
                console.log('Datos de ventas:', ventasData);

                // Procesar datos para Ventas Diarias
                const ventasPorDia = ventasData.reduce((acc, venta) => {
                    const fecha = venta.fecha.substring(0, 10); // Extraer solo la fecha (YYYY-MM-DD)

                    // Sumar el total de la venta
                    acc[fecha] = (acc[fecha] || 0) + parseFloat(venta.totalConIVA);
                    return acc;
                }, {});

                this.datosVentas = {
                    labels: Object.keys(ventasPorDia).sort(), // Ordenar fechas ascendente
                    valores: Object.values(ventasPorDia),
                };
                // Imprimir datos de ventas
                console.log('Ventas Labels:', this.datosVentas.labels);
                console.log('Ventas Values:', this.datosVentas.valores);
            } catch (error) {
                console.error('Error al cargar ventas:', error);
                throw error;
            }
        },

        crearGraficoStock() {
            const ctx = this.$refs.stockChart.getContext('2d');

            // Destruir gráfico existente si hay uno
            if (this.stockChart) {
                this.stockChart.destroy();
            }

            // Crear gráfico de stock por Almacén
            const almacenStock = this.datosStock.map(item => item.cantidadTotal);
            const almacenLabels = this.datosStock.map(item => item.nombreAlmacen);

            this.stockChart = new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: almacenLabels,
                    datasets: [
                        {
                            data: almacenStock,
                            backgroundColor: [
                                '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF', '#FF9F40', '#FF6384', '#36A2EB'
                            ]
                        }
                    ]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'right'
                        },
                        title: {
                            display: true,
                            text: 'Distribución de Stock por Almacén'
                        },
                        tooltip: {
                            callbacks: {
                                label: function (context) {
                                    const label = context.label || '';
                                    const value = context.raw || 0;
                                    const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                    const percentage = Math.round((value / total) * 100);
                                    return `${label}: ${value} (${percentage}%)`;
                                }
                            }
                        }
                    }
                }
            });
        },

        crearGraficoProductos() {
            const ctx = this.$refs.productoChart.getContext('2d');

            // Destruir gráfico existente si hay uno
            if (this.productoChart) {
                this.productoChart.destroy();
            }

            // Extraer datos 
            const productoStock = this.datosProductos.map(item => item.porcentaje);
            const productoLabels = this.datosProductos.map(item => item.nombreProducto);

            // Generar colores aleatorios para cada producto
            const randomColors = productoLabels.map(() =>
                `#${Math.floor(Math.random() * 16777215).toString(16)}`
            );

            this.productoChart = new Chart(ctx, {
                type: 'pie',
                data: {
                    labels: productoLabels,
                    datasets: [{
                        data: productoStock,
                        backgroundColor: randomColors
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: { position: 'right' },
                        title: {
                            display: true,
                            text: 'Distribución de Stock por Producto'
                        },
                        tooltip: {
                            callbacks: {
                                label: function (context) {
                                    const label = context.label || '';
                                    const value = context.raw || 0;
                                    return `${label}: ${value}%`;
                                }
                            }
                        }
                    }
                }
            });
        },

        crearGraficoVentas() {
            const ctx = this.$refs.ventasChart.getContext("2d");
            // Destruir gráfico existente si hay uno
            if (this.ventasChart) {
                this.ventasChart.destroy();
            }

            this.ventasChart = new Chart(ctx, {
                type: "line",
                data: {
                    labels: this.datosVentas.labels,
                    datasets: [
                        {
                            label: "Ventas Totales (€)",
                            data: this.datosVentas.valores,
                            borderColor: "rgba(75, 192, 192, 1)",
                            backgroundColor: "rgba(75, 192, 192, 0.2)",
                            borderWidth: 2,
                            fill: true,
                            tension: 0.3,
                        },
                    ],
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        x: { title: { display: true, text: "Fecha" } },
                        y: { title: { display: true, text: "Ventas (€)" } },
                    },
                },
            });
        },
    },
    mounted() {
        this.cargarDatos();
    },
    beforeUnmount() {
        // Limpiar gráficos al desmontar el componente
        if (this.stockChart) {
            this.stockChart.destroy();
        }
        if (this.productoChart) {
            this.productoChart.destroy();
        }
        if (this.ventasChart) {
            this.ventasChart.destroy();
        }
    }
};
</script>

<style scoped>
.chart-container {
    width: 100%;
    height: 400px;
}
</style>