// SPA fallback routing for webpack-dev-server
// Ensures direct URL access (e.g. /tools, /discover) returns index.html
// instead of "Cannot GET /tools"
//
// CMP webpack.config.d files are concatenated into the generated webpack.config.js
// BEFORE `module.exports = config`, so we must modify the `config` variable directly.
// Using `module.exports = { ... }` would be overwritten by the generated export.

(function ensureSpaRouting() {
    // Guard: if `config` is not defined yet, nothing to patch
    if (typeof config === 'undefined') return;

    // Ensure devServer object exists
    if (!config.devServer) {
        config.devServer = {};
    }

    // Set individual properties so we never blow away other plugin-set keys
    config.devServer.historyApiFallback = {
        rewrites: [
            { from: /\.(js|css|png|jpg|jpeg|gif|webp|svg|ico|json|wasm|woff|woff2|ttf|eot)$/, to: function(context) { return context.parsedUrl.pathname; } }
        ]
    };
    config.devServer.allowedHosts = 'all';
    if (!config.devServer.port) {
        config.devServer.port = 8081;
    }
    // Ensure static directory serves from dist
    if (!config.devServer.static) {
        config.devServer.static = false;
    }
})();
