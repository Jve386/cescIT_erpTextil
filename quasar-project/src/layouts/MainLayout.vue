<template>
  <q-layout view="hHh lpR fFf">
    <!-- Barra superior fija -->
    <q-header elevated class="bg-primary text-white" height-hint="60">
      <q-toolbar>
        <q-toolbar-title>
          TEXTIL CESC-IT
        </q-toolbar-title>
        <q-btn flat icon="menu" @click="toggleSidebar" />
      </q-toolbar>
    </q-header>

    <!-- Sidebar lateral -->
    <q-drawer v-model="leftDrawerOpen" side="left" bordered>
      <q-list>
        <q-item to="/agregarstock">
          <q-item-section avatar>
            <q-icon name="add_circle" />
          </q-item-section>
          <q-item-section>Agregar Stock</q-item-section>
        </q-item>

        <q-item to="/consultar-stock">
          <q-item-section avatar>
            <q-icon name="inventory" />
          </q-item-section>
          <q-item-section>Consultar Stock</q-item-section>
        </q-item>

        <q-item to="/ventas">
          <q-item-section avatar>
            <q-icon name="shopping_cart" />
          </q-item-section>
          <q-item-section>Ventas</q-item-section>
        </q-item>

        <q-item to="/gestionventas">
          <q-item-section avatar>
            <q-icon name="receipt_long" />
          </q-item-section>
          <q-item-section>Gestión de Ventas</q-item-section>
        </q-item>

        <q-item to="/estadisticas">
          <q-item-section avatar>
            <q-icon name="bar_chart" />
          </q-item-section>
          <q-item-section>Estadísticas</q-item-section>
        </q-item>
      </q-list>
    </q-drawer>

    <!-- Contenido principal -->
    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script>
export default {
  data() {
    return {
      leftDrawerOpen: false,  // Estado del sidebar
    };
  },
  methods: {
    toggleSidebar() {
      this.leftDrawerOpen = !this.leftDrawerOpen;
    },
  },
};
</script>
