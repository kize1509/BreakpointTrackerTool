    const wsProtocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const wsUrl = `${wsProtocol}//${window.location.host}/breakpoints`;
    const socket = new WebSocket(wsUrl);

    socket.onopen = function() {
        console.log('WebSocket connection established');
        socket.send(JSON.stringify({ping: true}));
            };

    socket.onmessage = function(event) {
        try {
            const data = JSON.parse(event.data);
            console.log('Received data:', data);
            renderBreakpoints(data);
        } catch (e) {
            console.error('Error parsing breakpoint data:', e);
            document.getElementById('breakpoints-container').innerHTML =
                `<div class="empty-state"><p>Error displaying breakpoints: ${e.message}</p></div>`;
        }
    };

    socket.onclose = function() {
        console.log('WebSocket connection closed');
    };

    function renderBreakpoints(data) {
        const container = document.getElementById('breakpoints-container');
        const timestamp = document.getElementById('timestamp');

        if (!data || !data.breakpoints || data.breakpoints.length === 0) {
            container.innerHTML = `
                <div class="empty-state">
                    <p>No breakpoints are set in this project</p>
                </div>
            `;
            timestamp.textContent = `Last updated: ${new Date().toLocaleTimeString()}`;
            return;
        }

        let html = `
            <table>
                <caption>Total number of breakpoints: ${data.breakpoints.length}</caption>
                <thead>

                    <tr>
                        <th>File</th>
                        <th>Line</th>
                        <th>Status</th>
                        <th>Type</th>
                    </tr>
                </thead>
                <tbody>
        `;

        data.breakpoints.forEach(bp => {
            const statusClass = bp.enabled ? 'status-enabled' : 'status-disabled';

            html += `
                <tr>
                    <td>${bp.fileName}</td>
                    <td>${bp.line}</td>
                    <td class="${statusClass}">${bp.enabled ? 'Enabled' : 'Disabled'}</td>
                    <td>${bp.type}</td>
                </tr>
            `;
        });

        html += `
                </tbody>
            </table>
        `;

        container.innerHTML = html;
        timestamp.textContent = `Last updated: ${new Date().toLocaleTimeString()}`;
    }