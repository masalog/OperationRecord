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
                <td>${r.startDateTime}</td>
                <td>${r.startMeter}</td>
                <td>${r.endDateTime ?? ""}</td>
                <td>${r.endMeter ?? ""}</td>
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