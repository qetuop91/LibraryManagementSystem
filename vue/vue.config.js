const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,
    productionSourceMap: false,
    outputDir: 'dist',
    publicPath: './',
    devServer: {
        //关闭报错遮罩
        client: {
            overlay: false
        },
    }
})

