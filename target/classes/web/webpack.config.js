var webpack = require("webpack");
module.exports = {
    context: __dirname + '/app',
    entry: './app.module.js',
    output: {
        path : __dirname + '/app',
        filename: 'mail.app.js'
    },
    devtool: 'cheap-inline-module-source-map',

    resolve: {
        alias: {
            jquery: "jquery/src/jquery"
        }
    }
};