document.addEventListener('DOMContentLoaded', function() {

    if (window.motosPorEstadoData) {
        const estadoLabels = window.motosPorEstadoData.map(item => item.estado);
        const estadoData = window.motosPorEstadoData.map(item => item.total);

        new Chart(document.getElementById('motosPorEstadoChart').getContext('2d'), {
            type: 'doughnut',
            data: {
                labels: estadoLabels,
                datasets: [{
                    label: 'Quantidade',
                    data: estadoData,
                    backgroundColor: ['rgba(255, 99, 132, 0.7)', 'rgba(255, 193, 7, 0.7)', 'rgba(157, 255, 0, 0.7)'],
                    borderColor: ['#ff6384', '#ffc107', '#9dff00'],
                    borderWidth: 1
                }]
            },
            options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { position: 'top', labels: { color: '#fff' } } } }
        });
    }

    if (window.motosPorModeloData) {
        const modeloLabels = window.motosPorModeloData.map(item => item.modelo);
        const modeloData = window.motosPorModeloData.map(item => item.total);

        new Chart(document.getElementById('motosPorModeloChart').getContext('2d'), {
            type: 'bar',
            data: {
                labels: modeloLabels,
                datasets: [{
                    label: 'Quantidade de Motos',
                    data: modeloData,
                    backgroundColor: ['rgba(153, 102, 255, 0.7)', 'rgba(157, 255, 0, 0.7)', 'rgba(54, 162, 235, 0.7)'],
                    borderColor: ['#9966ff', '#9dff00', '#36a2eb'],
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { display: false } },
                scales: {
                    y: { ticks: { color: '#fff', stepSize: 1 } },
                    x: { ticks: { color: '#fff' } }
                }
            }
        });
    }
});