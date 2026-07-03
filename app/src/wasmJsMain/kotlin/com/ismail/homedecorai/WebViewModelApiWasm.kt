package com.ismail.homedecorai

actual fun getAnonymousIdFromPlatform(): String {
    val id = localStorageGet("hd_anonymous_id")
    if (id.isEmpty() || id == "null") {
        val newId = "anon_" + toBase36(jsRandom()) + "_" + toBase36(jsNow())
        localStorageSet("hd_anonymous_id", newId)
        return newId
    }
    return id
}

private fun toBase36(value: Double): String {
    val chars = "0123456789abcdefghijklmnopqrstuvwxyz"
    val num = kotlin.math.abs(value.toLong())
    if (num == 0L) return "0"
    var result = ""
    var n = num
    while (n > 0) {
        result = chars[(n % 36).toInt()] + result
        n /= 36
    }
    return result
}

/**
 * Launches an async JS operation and polls until it completes.
 * Kotlin/WASM doesn't have Promise.await(), so we store the result
 * in a global variable and poll with Thread.sleep-like delay.
 */
private fun launchAsyncAndWait(block: () -> Unit): String {
    // Set result slot to "pending"
    setResultSlot("")
    setRequestPending(true)
    block()
    // Poll until JS sets the result
    var attempts = 0
    while (getRequestPending() && attempts < 100) {
        jsThreadSleep(50)
        attempts++
    }
    return getResultSlot()
}

actual suspend fun convexQuery(path: String, args: Map<String, Any?>): String {
    val argsJson = encodeArgs(args)
    return launchAsyncAndWait {
        jsFetchConvexAsync("query", path, argsJson)
    }
}

actual suspend fun convexMutation(path: String, args: Map<String, Any?>): String {
    val argsJson = encodeArgs(args)
    return launchAsyncAndWait {
        jsFetchConvexAsync("mutation", path, argsJson)
    }
}

actual suspend fun convexQueryAuth(path: String, args: Map<String, Any?>): String {
    val argsJson = encodeArgs(args)
    return launchAsyncAndWait {
        jsFetchConvexAuthAsync("query", path, argsJson)
    }
}

actual suspend fun convexMutationAuth(path: String, args: Map<String, Any?>): String {
    val argsJson = encodeArgs(args)
    return launchAsyncAndWait {
        jsFetchConvexAuthAsync("mutation", path, argsJson)
    }
}

actual suspend fun clerkInit(): String {
    val key = documentGetMeta("hd-clerk-key")
    if (key.isEmpty()) return "no_key"
    return launchAsyncAndWait { jsClerkInitAsync(key) }
}

actual suspend fun clerkSignIn(): String {
    return launchAsyncAndWait { jsClerkActionAsync("signIn") }
}

actual suspend fun clerkSignUp(): String {
    return launchAsyncAndWait { jsClerkActionAsync("signUp") }
}

actual suspend fun clerkSignOut(): String {
    return launchAsyncAndWait { jsClerkActionAsync("signOut") }
}

actual suspend fun clerkGetUser(): ClerkUserData? {
    val json = launchAsyncAndWait { jsClerkActionAsync("getUser") }
    if (json == "null" || json.isEmpty()) return null
    return parseClerkUser(json)
}

private fun parseClerkUser(json: String): ClerkUserData? {
    return try {
        ClerkUserData(
            id = extractJsonString(json, "id"),
            firstName = extractJsonString(json, "firstName"),
            lastName = extractJsonString(json, "lastName"),
            email = extractJsonString(json, "email"),
        )
    } catch (_: Exception) {
        null
    }
}

private fun extractJsonString(json: String, key: String): String {
    val match = Regex("\"$key\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"").find(json) ?: return ""
    return match.groupValues[1]
        .replace("\\\"", "\"")
        .replace("\\\\", "\\")
        .replace("\\n", "\n")
        .replace("\\t", "\t")
}

private fun encodeArgs(args: Map<String, Any?>): String {
    if (args.isEmpty()) return "{}"
    val entries = args.entries.joinToString(",") { (key, value) ->
        val encoded = when (value) {
            is String -> "\"${value.replace("\\", "\\\\").replace("\"", "\\\"")}\""
            is Number -> value.toString()
            is Boolean -> value.toString()
            is List<*> -> {
                val items = value.joinToString(",") { item ->
                    when (item) {
                        is String -> "\"${item.replace("\\", "\\\\").replace("\"", "\\\"")}\""
                        is Number -> item.toString()
                        is Boolean -> item.toString()
                        else -> "null"
                    }
                }
                "[$items]"
            }
            null -> "null"
            else -> "\"$value\""
        }
        "\"$key\":$encoded"
    }
    return "{$entries}"
}

actual suspend fun convexCreateUploadUrl(anonymousId: String): String {
    return convexMutationAuth("generations:createSourceUploadUrl", mapOf("anonymousId" to anonymousId))
}

actual suspend fun convexUploadToStorage(uploadUrl: String, fileBase64: String, mimeType: String): String {
    return launchAsyncAndWait { jsUploadToStorageAsync(uploadUrl, fileBase64, mimeType) }
}

actual fun browserDownloadFile(url: String, filename: String) {
    jsTriggerDownload(url, filename)
}

actual fun browserShareContent(title: String, url: String) {
    jsTriggerShare(title, url)
}

// ---- JS interop: shared result slot ----

@JsFun("() => window._hdResult || ''")
private external fun getResultSlot(): String

@JsFun("(v) => { window._hdResult = v; }")
private external fun setResultSlot(v: String)

@JsFun("() => window._hdPending || false")
private external fun getRequestPending(): Boolean

@JsFun("(v) => { window._hdPending = v; }")
private external fun setRequestPending(v: Boolean)

@JsFun("(ms) => { /* no-op, polling handles timing */ }")
private external fun jsThreadSleep(ms: Int)

// ---- JS interop: Convex fetch (sets result slot on completion) ----

@JsFun("""(method, path, argsJson) => {
    fetch(window._hdConvexUrl + '/api/' + method, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ path: path, args: JSON.parse(argsJson || '{}') })
    })
    .then(function(r) {
        if (!r.ok) return r.text().then(function(t) { throw new Error(t); });
        return r.json();
    })
    .then(function(res) {
        window._hdResult = JSON.stringify(res);
        window._hdPending = false;
    })
    .catch(function(e) {
        window._hdResult = '';
        window._hdPending = false;
    });
}""")
private external fun jsFetchConvexAsync(method: String, path: String, argsJson: String)

@JsFun("""(method, path, argsJson) => {
    var headers = { 'Content-Type': 'application/json' };
    var tokenP = (window.Clerk && window.Clerk.session)
        ? window.Clerk.session.getToken({ template: 'convex' })
        : Promise.resolve('');
    tokenP.then(function(token) {
        if (token) headers['Authorization'] = 'Bearer ' + token;
        return fetch(window._hdConvexUrl + '/api/' + method, {
            method: 'POST',
            headers: headers,
            body: JSON.stringify({ path: path, args: JSON.parse(argsJson || '{}') })
        });
    })
    .then(function(r) {
        if (!r.ok) return r.text().then(function(t) { throw new Error(t); });
        return r.json();
    })
    .then(function(res) {
        window._hdResult = JSON.stringify(res);
        window._hdPending = false;
    })
    .catch(function(e) {
        window._hdResult = '';
        window._hdPending = false;
    });
}""")
private external fun jsFetchConvexAuthAsync(method: String, path: String, argsJson: String)

// ---- JS interop: Convex storage upload ----

@JsFun("""(uploadUrl, base64, mimeType) => {
    var raw = atob(base64);
    var arr = new Uint8Array(raw.length);
    for (var i = 0; i < raw.length; i++) arr[i] = raw.charCodeAt(i);
    var blob = new Blob([arr], { type: mimeType });
    fetch(uploadUrl, {
        method: 'PUT',
        headers: { 'Content-Type': mimeType },
        body: blob
    })
    .then(function(r) {
        if (!r.ok) return r.text().then(function(t) { throw new Error(t); });
        return r.json();
    })
    .then(function(res) {
        window._hdResult = JSON.stringify(res);
        window._hdPending = false;
    })
    .catch(function(e) {
        window._hdResult = '';
        window._hdPending = false;
    });
}""")
private external fun jsUploadToStorageAsync(uploadUrl: String, base64: String, mimeType: String)

// ---- JS interop: browser download ----

@JsFun("""(url, filename) => {
    var a = document.createElement('a');
    a.href = url;
    a.download = filename || 'homedecor-design.png';
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
}""")
private external fun jsTriggerDownload(url: String, filename: String)

@JsFun("""(title, url) => {
    if (navigator.share) {
        navigator.share({ title: title, url: url }).catch(function() {});
    } else {
        var a = document.createElement('a');
        a.href = url;
        a.target = '_blank';
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
    }
}""")
private external fun jsTriggerShare(title: String, url: String)

// ---- JS interop: localStorage ----

@JsFun("(key) => { try { return localStorage.getItem(key) || ''; } catch(e) { return ''; } }")
private external fun localStorageGet(key: String): String

@JsFun("(key, value) => { try { localStorage.setItem(key, value); } catch(e) {} }")
private external fun localStorageSet(key: String, value: String)

// ---- JS interop: Math.random / Date.now ----

@JsFun("() => Math.random()")
private external fun jsRandom(): Double

@JsFun("() => Date.now()")
private external fun jsNow(): Double

// ---- JS interop: document meta ----

@JsFun("(name) => { var el = document.querySelector('meta[name=\"' + name + '\"]'); return el ? el.getAttribute('content') || '' : ''; }")
private external fun documentGetMeta(name: String): String

// ---- JS interop: Clerk ----

@JsFun("""(key) => {
    new Promise(function(resolve, reject) {
        var script = document.createElement('script');
        script.src = 'https://cdn.jsdelivr.net/npm/@clerk/clerk-js@5.56.0/dist/clerk.browser.js';
        script.async = true;
        script.onload = function() {
            try {
                window.Clerk.initialize({ publishableKey: key });
                window._hdResult = 'ok';
            } catch(e) {
                window._hdResult = 'error:' + (e.message || String(e));
            }
            window._hdPending = false;
        };
        script.onerror = function() {
            window._hdResult = 'error:load_failed';
            window._hdPending = false;
        };
        document.head.appendChild(script);
    });
}""")
private external fun jsClerkInitAsync(key: String)

@JsFun("""(action) => {
    if (action === 'signIn') {
        if (!window.Clerk) { window._hdResult = 'error:no_clerk'; window._hdPending = false; return; }
        window.Clerk.openSignIn({}).then(function() {
            window._hdResult = 'ok';
            window._hdPending = false;
        }).catch(function(e) {
            window._hdResult = 'error:' + (e.message || String(e));
            window._hdPending = false;
        });
    } else if (action === 'signUp') {
        if (!window.Clerk) { window._hdResult = 'error:no_clerk'; window._hdPending = false; return; }
        window.Clerk.openSignUp({}).then(function() {
            window._hdResult = 'ok';
            window._hdPending = false;
        }).catch(function(e) {
            window._hdResult = 'error:' + (e.message || String(e));
            window._hdPending = false;
        });
    } else if (action === 'signOut') {
        if (!window.Clerk) { window._hdResult = 'error:no_clerk'; window._hdPending = false; return; }
        window.Clerk.signOut({}).then(function() {
            window._hdResult = 'ok';
            window._hdPending = false;
        }).catch(function(e) {
            window._hdResult = 'error:' + (e.message || String(e));
            window._hdPending = false;
        });
    } else if (action === 'getUser') {
        if (!window.Clerk || !window.Clerk.user) {
            window._hdResult = 'null';
            window._hdPending = false;
            return;
        }
        var u = window.Clerk.user;
        var email = (u.emailAddresses && u.emailAddresses[0]) ? u.emailAddresses[0].emailAddress : '';
        window._hdResult = JSON.stringify({ id: u.id, firstName: u.firstName || '', lastName: u.lastName || '', email: email });
        window._hdPending = false;
    } else {
        window._hdResult = 'error:unknown';
        window._hdPending = false;
    }
}""")
private external fun jsClerkActionAsync(action: String)
