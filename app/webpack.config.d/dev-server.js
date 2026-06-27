// SPA fallback routing for webpack-dev-server
// Ensures direct URL access (e.g. /tools, /discover) returns index.html
// instead of "Cannot GET /tools"
//
// CMP webpack.config.d files are concatenated into the generated webpack.config.js
// BEFORE `module.exports = config`, so we must modify the `config` variable directly.

(function ensureSpaRouting() {
    if (typeof config === 'undefined') return;

    var oldDevServer = config.devServer || {};
    config.devServer = {};

    // Copy over CMP settings we want to keep
    if (oldDevServer.client) config.devServer.client = oldDevServer.client;
    if (oldDevServer.open !== undefined) config.devServer.open = oldDevServer.open;
    if (oldDevServer.host) config.devServer.host = oldDevServer.host;
    config.devServer.static = oldDevServer.static || false;
    config.devServer.allowedHosts = 'all';
    config.devServer.port = oldDevServer.port || 8081;

    // Use setupMiddlewares to inject SPA fallback at the very front of the
    // middleware stack. This runs BEFORE any static-file or historyApiFallback
    // middleware, so it catches every request that doesn't match a real file.
    config.devServer.setupMiddlewares = function(middlewares, devServer) {
        // Add SPA fallback as the FIRST middleware
        middlewares.unshift({
            name: 'spa-fallback',
            middleware: function(req, res, next) {
                // Skip requests for actual files (contain a dot in the last path segment)
                var pathname = req.url.split('?')[0];
                var lastSegment = pathname.split('/').pop();
                if (lastSegment && lastSegment.includes('.')) {
                    return next();
                }
                // Rewrite to index.html
                req.url = '/index.html';
                next();
            }
        });
        return middlewares;
    };
})();
