import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import '@/assets/global.css';
import SlideVerify from 'vue-monoplasty-slide-verify';
import axios from "axios";
import {getToken} from "@/utils/auth";

axios.defaults.headers.common['token'] = getToken()
Vue.config.productionTip = false
Vue.use(ElementUI, {size: 'small'});
Vue.use(SlideVerify)


new Vue({
    router,
    render: h => h(App)
}).$mount('#app')
