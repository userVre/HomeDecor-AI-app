// Convex URL bootstrap — sets window._hdConvexUrl from meta tag
// All other logic (Clerk, Convex HTTP) lives in WebViewModelApiWasm.kt @JsFun functions
(function() {
  'use strict';
  var metaConvex = document.querySelector('meta[name="hd-convex-url"]');
  window._hdConvexUrl = metaConvex ? metaConvex.getAttribute('content') : '';
})();
