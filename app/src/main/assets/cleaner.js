(function() {
    if (window.marekothInjected) return;
    window.marekothInjected = true;

    let cleaned = 0;
    let scanned = 0;
    let running = false;
    let delayInterval = 2.5; // senin ayarındaki gibi

    function updateStats(status) {
        try {
            AndroidBridge.onStats(cleaned, scanned, status);
        } catch(e) {}
    }

    function randomJitter() {
        return (Math.random() * 0.7 * 2 - 0.7); // ±0.7s
    }

    async function sleep(ms) {
        return new Promise(r => setTimeout(r, ms));
    }

    // Mobil ve desktop için uyumlu selectorlar
    function getRepostButtons() {
        // TikTok web'de repost kaldırma butonu - hem m.tiktok hem www için
        return document.querySelectorAll('[data-e2e="repost-button"], [data-e2e="repost-action"], svg[data-e2e="repost-icon"]');
    }

    // Daha genel: Reposts sekmesindeki videolar
    function getVideos() {
        return document.querySelectorAll('div[data-e2e="user-post-item"], div[data-e2e="user-repost-item"], a[href*="/video/"]');
    }

    async function cleanLoop() {
        while (running) {
            const buttons = getRepostButtons();
            if (buttons.length === 0) {
                updateStats("Scanning... No reposts found, scrolling");
                window.scrollBy(0, 800);
                await sleep(1500);
                scanned++;
                updateStats("Scanning...");
                continue;
            }

            for (let btn of buttons) {
                if (!running) break;
                try {
                    // TikTok'ta repost'u kaldırmak için butona tıkla -> confirm
                    btn.click();
                    await sleep(800 + randomJitter()*1000);

                    // Confirm popup - "Remove repost" butonu
                    let confirmBtn = document.querySelector('button:has-text("Remove"), [data-e2e="repost-remove"], button[class*="confirm"]');
                    // fallback: metin ile ara
                    if (!confirmBtn) {
                        let allBtns = document.querySelectorAll('button');
                        for (let b of allBtns) {
                            if (b.innerText && b.innerText.toLowerCase().includes('remove repost')) {
                                confirmBtn = b; break;
                            }
                        }
                    }

                    if (confirmBtn) {
                        confirmBtn.click();
                        cleaned++;
                        updateStats("Cleaning...");
                    }
                } catch(e) { console.log(e); }

                let delay = (delayInterval + randomJitter()) * 1000;
                await sleep(delay);
                scanned++;
                updateStats("Running...");
            }

            window.scrollBy(0, 600);
            await sleep(1000);
        }
        updateStats("Stopped");
    }

    window.startMarekothCleaner = function() {
        if (running) return;
        running = true;
        cleaned = 0; scanned = 0;
        updateStats("Running...");
        cleanLoop();
    }

    window.stopMarekothCleaner = function() {
        running = false;
    }

    console.log("Marekoth Cleaner injected - mobile ready");
})();