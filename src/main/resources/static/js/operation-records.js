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
                <td>${r.title}</td>
                <td>${r.description}</td>
                <td>
                    <button onclick="deleteRecord(${r.id})">削除</button>
                </td>
            </tr>
        `;
        tbody.insertAdjacentHTML("beforeend", row);
    });
}

// ---------------------------
// 新規登録
// ---------------------------
document.getElementById("createForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const data = {
        title: document.getElementById("title").value,
        description: document.getElementById("description").value
    };

    await fetch("/operation-records", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(data)
    });

    loadRecords();
    e.target.reset();
});

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