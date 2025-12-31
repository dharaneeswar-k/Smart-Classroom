console.log('Testing backend connection...');
api.get('/auth/health-check') // Assuming this endpoint exists or similar
    .then(res => console.log('Backend Online', res))
    .catch(err => console.error('Backend Offline', err));
