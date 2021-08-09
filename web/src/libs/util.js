export default {
  formatAge: function (age) {
    if (age) {
      return age + "岁";
    } else {
      return "无";
    }
  },
  formatAuth: function (auth) {
    let authStr;
    switch (auth) {
      case 4:
        authStr = "群主";
        break;
      case 2:
        authStr = "管理员";
        break;
      case 1:
        authStr = "群成员";
        break;
      default:
        authStr = "无";
    }
    return authStr;
  },
  formatGender(gender) {
    let genderStr;
    switch (gender) {
      case 0:
        genderStr = "男";
        break;
      case 1:
        genderStr = "女";
        break;
      default:
        genderStr = "无";
    }
    return genderStr;
  },
  costTime(start, end) {
    let millSeconds = (end - start);
    if (millSeconds < 1000) {
      return millSeconds + "毫秒";
    }
    let seconds = millSeconds / 1000;
    if (seconds < 60) {
      return seconds + "秒";
    }
    let mins = (seconds / 60).toFixed(0);
    seconds = (seconds % 60).toFixed(0);
    return mins + "分" + seconds + "秒";
  }
}
