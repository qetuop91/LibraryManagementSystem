import axios from "axios";
import {getToken} from "@/utils/auth.js"
import Cookies from "js-cookie";

const request = axios.create({
    // baseURL: 'http://10.23.127.99:9090',
    baseURL: 'http://10.23.127.99:8080/LibraryManagement/api',
    // baseURL: 'http://localhost:8080/api',
    timeout: 5000
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    config.headers['token'] = getToken(); //设置请求头
    if(localStorage.getItem("token")) {
        config.headers.Authorization = localStorage.getItem("token")
    }
    //新增
    config.headers["Access-Control-Allow-Origin"] = "*";



    return config
}, error => {
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        return res;
    },
    error => {
        console.log('err' + error) // for debug
        return Promise.reject(error)
    }
)

export default request