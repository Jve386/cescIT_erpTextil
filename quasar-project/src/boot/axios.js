import { defineBoot } from '#q-app/wrappers'
import axios from 'axios'

const api = axios.create({ 
  baseURL: 'http://localhost:8080/api' 
})

// Add request interceptor for debugging
api.interceptors.request.use(config => {
  console.log('API Request:', config.method.toUpperCase(), config.baseURL + config.url);
  return config;
});

// Add response interceptor for debugging
api.interceptors.response.use(
  response => {
    console.log('API Response:', response.status, response.config.url);
    return response;
  },
  error => {
    console.error('API Error:', error.response ? error.response.status : 'No response', 
                 error.config ? error.config.url : 'No URL');
    return Promise.reject(error);
  }
);

export default defineBoot(({ app }) => {
  app.config.globalProperties.$axios = axios

  app.config.globalProperties.$api = api
})

export { api }
