// SPA fallback routing for webpack-dev-server
// Ensures direct URL access (e.g. /tools, /discover) returns index.html
// instead of "Cannot GET /tools"
//
// CMP webpack.config.d files are concatenated into the generated webpack.config.js
// BEFORE `module.exports = config`, so we must modify the `config` variable directly.
// Using `module.exports = { ... }` would be overwritten by the generated export.

config.devServer = Object.assign(
    {},
    config.devServer || {},
    {
        historyApiFallback: true,
        allowedHosts: 'all',
    }
);
