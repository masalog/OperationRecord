// ---------------------------
// 日時フォーマット（1/5(金) 14:30）
// ---------------------------
function formatDateTime(dt) {
    if (!dt) return "";

    const d = new Date(dt);

    const month = d.getMonth() + 1;
    const day = d.getDate();
    const hours = String(d.getHours()).padStart(2, "0");
    const minutes = String(d.getMinutes()).padStart(2, "0");

    const week = ["日", "月", "火", "水", "木", "金", "土"];
    const w = week[d.getDay()];

    return `${month}/${day}(${w}) ${hours}:${minutes}`;
}

// ---------------------------
// 所要時間（発車→到着）
// ---------------------------
function calcDuration(start, end) {
    if (!start || !end) return "";

    const s = new Date(start);
    const e = new Date(end);
    const diff = e - s;

    if (isNaN(diff)) return "";

    const hours = Math.floor(diff / (1000 * 60 * 60));
    const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));

    return `${hours}時間${minutes}分`;
}

// ---------------------------
// 走行距離（メーター差）
// ---------------------------
function calcDistance(startMeter, endMeter) {
    if (startMeter == null || endMeter == null) return "";
    return (endMeter - startMeter) + "km";
}

// ---------------------------
// 一覧取得
// ---------------------------
async function loadRecords() {
    const res = await fetch("/operation-records");
    const records = await res.json();

    const tbody = document.getElementById("recordTable");
    tbody.innerHTML = "";

    records.forEach(r => {
        const row = `
            <tr>
                <td>${r.vehicleId}</td>
                <td>${r.driverId}</td>
                <td>${formatDateTime(r.startDateTime)}</td>
                <td>${r.startMeter}km</td>
                <td>${r.endMeter != null ? r.endMeter + "km" : ""}</td>
                <td>${formatDateTime(r.endDateTime)}</td>

                <td>
                    <button onclick="deleteRecord(${r.id})">削除</button>
                </td>
            </tr>
        `;
        tbody.insertAdjacentHTML("beforeend", row);
    });
}

// ---------------------------
// 削除
// ---------------------------
async function deleteRecord(id) {
    await fetch(`/operation-records/${id}`, {
        method: "DELETE"
    });

    loadRecords();
}

// 初期ロード
loadRecords();