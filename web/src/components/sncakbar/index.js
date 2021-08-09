import Vue from 'vue'
import Snackbar from './Snackbar'

const SnackbarConstructor = Vue.extend(Snackbar)

function openSnackbar(options) {
  if (options === undefined || options === null) {
    options = {
      message: '',
      position: 'top',
      open: false,
      time: 3000,
    }
  } else if (typeof options === 'string' || typeof options === 'number') {
    options = {
      message: options,
      position: 'top',
      open: false,
      time: 3000,
    }
  }
  const snackbarDom = new SnackbarConstructor({
    el: document.createElement('div'),
    data() {
      return options;
    }
  });
  document.body.appendChild(snackbarDom.$el);
  snackbarDom.open = true;
}

export default openSnackbar
