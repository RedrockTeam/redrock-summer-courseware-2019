/* 请求天气
https://restapi.amap.com/v3/weather/weatherInfo?key=${key}&city=500107

请求ip对应地址
https://restapi.amap.com/v3/ip?key=${key}

经纬度转详细地址
https://restapi.amap.com/v3/geocode/regeo?key=${key}&location=106.2832832,29.36962828
*/
const axios = require('axios')
const {key} = require('./key.js')

axios.get(`https://restapi.amap.com/v3/ip?key=${key}`)
    .then(res => {
        const rightTop = res.data.rectangle.split(';')[1]
        return axios.get(`https://restapi.amap.com/v3/geocode/regeo?key=${key}&location=${rightTop}`)
    })
    .then(res => {
        const adcode = res.data.regeocode.addressComponent.adcode
        return axios.get(`https://restapi.amap.com/v3/weather/weatherInfo?key=${key}&city=${adcode}`)
    })
    .then(res => console.log(res.data))
    .catch(err => console.log(err))

async function getData() {
    const data1 = await axios.get(`https://restapi.amap.com/v3/ip?key=${key}`)
    const rightTop = data1.data.rectangle.split(';')[1]
    const data2 = await axios.get(`https://restapi.amap.com/v3/geocode/regeo?key=${key}&location=${rightTop}`)
    const adcode = data2.data.regeocode.addressComponent.adcode
    const data3 = await axios.get(`https://restapi.amap.com/v3/weather/weatherInfo?key=${key}&city=${adcode}`)
    console.log(data3.data)
}

getData()

