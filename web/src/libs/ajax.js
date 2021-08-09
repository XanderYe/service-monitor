//引入axios
import axios from 'axios'
import {router} from '../router/index.js'
import openSnackbar from '../components/sncakbar'

axios.defaults.baseURL = ajaxUrl;
//超时请求
axios.defaults.timeout = 1000000;
//响应拦截器即异常处理
axios.interceptors.response.use(response => {
  return response
}, err => {
  console.log(err);
  openSnackbar("连接到服务器失败");
  return Promise.reject(err.response);
});

const requests = {

  getToken() {
    return localStorage.getItem("token") || "";
  },

  //get请求
  get(url, param) {
    return new Promise((resolve, reject) => {
      axios({
        method: 'get',
        url,
        params: param,
        headers: {
          "token": this.getToken()
        }
      }).then(res => {
        this.error(res.data.code);
        resolve(res)
      }).catch((err) => {
        if (err) {
          this.error(err.data.status);
        }
        reject(err);
      })
    })
  },
  //post请求
  post(url, param) {
    return new Promise((resolve, reject) => {
      axios({
        method: 'post',
        url,
        data: param,
        headers: {
          "token": this.getToken()
        }
      }).then(res => {
        this.error(res.data.code);
        resolve(res);
      }).catch((err) => {
        if (err) {
          this.error(err.data.status);
        }
        reject(err);
      })
    })
  },

  //post请求
  upload(url, param) {
    return new Promise((resolve, reject) => {
      axios({
        method: 'post',
        url,
        data: param,
        headers: {
          "content-type": "multipart/form-data",
          "token": this.getToken()
        }
      }).then(res => {
        this.error(res.data.code);
        resolve(res);
      }).catch((err) => {
        if (err) {
          this.error(err.data.status);
        }
        reject(err);
      })
    })
  },

  postAndGetFile(url, param) {
    return new Promise((resolve, reject) => {
      axios({
        method: 'post',
        url,
        data: param,
        headers: {
          "token": this.getToken()
        },
        responseType: 'blob'
      }).then(res => {
        this.error(res.data.code);
        resolve(res);
      }).catch((err) => {
        if (err) {
          this.error(err.data.status);
        }
        reject(err);
      })
    })
  },

  getFiles(url, params, fileName) {
    fileName = fileName || "";
    return new Promise((resolve, reject) => {
      axios.get(url, {
        headers: {
          "token": this.getToken()
        },
        params: params,
        responseType: 'blob',
      }).then(res => {
        this.error(res.data.code);
        let url = window.URL.createObjectURL(res.data);
        let link = document.createElement('a');
        link.style.display = 'none';
        link.href = url;
        link.setAttribute('download', fileName);
        document.body.appendChild(link);
        link.click();
        resolve(res);
      }).catch((err) => {
        if (err) {
          this.error(err.data.status);
        }
        reject(err);
      })
    })
  },

  error(status) {
    switch (status) {
      //请求没有权限状态码
      case 20001:
        router.replace({
          path: '/login'
        });
        break;
      case 2:
        router.replace({
          path: '/error-403'
        });
        break;
      //请求未找到
      case 404:
        router.replace({
          path: '/error-404'
        });
        break;
    }
  }


}
export default requests;
