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
                <td>${r.id}</td>
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