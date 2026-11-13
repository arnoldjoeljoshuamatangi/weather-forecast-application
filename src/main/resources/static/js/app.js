// Application State
let currentCity = 'London';
let currentUnit = 'metric'; // 'metric' (°C) or 'imperial' (°F)
let autocompleteDebounceTimer = null;

document.addEventListener('DOMContentLoaded', () => {
    initApp();
});

function initApp() {
    setupEventListeners();
    fetchWeather(currentCity, currentUnit);
}

function setupEventListeners() {
    // Search Button & Input
    const searchBtn = document.getElementById('searchBtn');
    const searchInput = document.getElementById('citySearchInput');

    searchBtn.addEventListener('click', () => {
        handleSearch();
    });

    searchInput.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') {
            e.preventDefault();
            handleSearch();
        }
    });

    // Autocomplete Debounce
    searchInput.addEventListener('input', (e) => {
        clearTimeout(autocompleteDebounceTimer);
        const query = e.target.value.trim();
        if (query.length >= 2) {
            autocompleteDebounceTimer = setTimeout(() => {
                fetchAutocomplete(query);
            }, 300);
        } else {
            hideAutocomplete();
        }
    });

    // Close autocomplete on click outside
    document.addEventListener('click', (e) => {
        if (!e.target.closest('.search-box-wrapper')) {
            hideAutocomplete();
        }
    });

    // Quick City Tags
    const cityTags = document.querySelectorAll('.city-tag');
    cityTags.forEach(tag => {
        tag.addEventListener('click', () => {
            const city = tag.getAttribute('data-city');
            currentCity = city;
            searchInput.value = '';
            fetchWeather(city, currentUnit);
        });
    });

    // Unit Toggle
    const unitMetric = document.getElementById('unitMetric');
    const unitImperial = document.getElementById('unitImperial');

    unitMetric.addEventListener('click', () => {
        if (currentUnit !== 'metric') {
            currentUnit = 'metric';
            unitMetric.classList.add('active');
            unitImperial.classList.remove('active');
            fetchWeather(currentCity, currentUnit);
        }
    });

    unitImperial.addEventListener('click', () => {
        if (currentUnit !== 'imperial') {
            currentUnit = 'imperial';
            unitImperial.classList.add('active');
            unitMetric.classList.remove('active');
            fetchWeather(currentCity, currentUnit);
        }
    });

    // Locate Me (HTML5 Geolocation)
    const locationBtn = document.getElementById('locationBtn');
    locationBtn.addEventListener('click', handleGeolocation);
}

function handleSearch() {
    const searchInput = document.getElementById('citySearchInput');
    const city = searchInput.value.trim();
    if (city) {
        currentCity = city;
        hideAutocomplete();
        fetchWeather(city, currentUnit);
    }
}

// Fetch Weather Data
async function fetchWeather(city, unit) {
    showLoading(true);
    try {
        const url = `/api/v1/weather/report?city=${encodeURIComponent(city)}&unit=${unit}`;
        const response = await fetch(url);
        const json = await response.json();

        if (response.ok && json.success) {
            renderWeatherDashboard(json.data);
        } else {
            showToast(json.message || 'City not found');
        }
    } catch (err) {
        showToast('Error fetching weather: ' + err.message);
    } finally {
        showLoading(false);
    }
}

// Fetch by GPS Coordinates
function handleGeolocation() {
    if (!navigator.geolocation) {
        showToast('Geolocation is not supported by your browser');
        return;
    }

    showToast('Detecting current coordinates...');
    navigator.geolocation.getCurrentPosition(
        async (position) => {
            const lat = position.coords.latitude;
            const lon = position.coords.longitude;
            showLoading(true);
            try {
                const url = `/api/v1/weather/coordinates?lat=${lat}&lon=${lon}&unit=${currentUnit}`;
                const response = await fetch(url);
                const json = await response.json();
                if (response.ok && json.success) {
                    currentCity = json.data.location.name || 'Current Location';
                    renderWeatherDashboard(json.data);
                    showToast('Weather updated for current location');
                } else {
                    showToast('Failed to resolve coordinates');
                }
            } catch (err) {
                showToast('Error: ' + err.message);
            } finally {
                showLoading(false);
            }
        },
        (error) => {
            showToast('Unable to retrieve location: ' + error.message);
        }
    );
}

// Autocomplete Search
async function fetchAutocomplete(query) {
    try {
        const res = await fetch(`/api/v1/weather/search?query=${encodeURIComponent(query)}`);
        const json = await res.json();
        if (res.ok && json.success && json.data && json.data.length > 0) {
            renderAutocomplete(json.data);
        } else {
            hideAutocomplete();
        }
    } catch (e) {
        hideAutocomplete();
    }
}

function renderAutocomplete(items) {
    const dropdown = document.getElementById('autocompleteDropdown');
    dropdown.innerHTML = '';
    items.forEach(loc => {
        const div = document.createElement('div');
        div.className = 'autocomplete-item';
        div.innerHTML = `
            <div>
                <span class="item-city">${escapeHtml(loc.name)}</span>
                <span class="item-region">${loc.admin1 ? escapeHtml(loc.admin1) + ', ' : ''}${escapeHtml(loc.country)}</span>
            </div>
            <i class="fa-solid fa-arrow-turn-up" style="color: #64748b; font-size: 0.8rem;"></i>
        `;
        div.addEventListener('click', () => {
            currentCity = loc.name;
            document.getElementById('citySearchInput').value = loc.name;
            hideAutocomplete();
            fetchWeather(loc.name, currentUnit);
        });
        dropdown.appendChild(div);
    });
    dropdown.classList.remove('hidden');
}

function hideAutocomplete() {
    const dropdown = document.getElementById('autocompleteDropdown');
    dropdown.classList.add('hidden');
    dropdown.innerHTML = '';
}

// Render Dashboard
function renderWeatherDashboard(data) {
    const loc = data.location;
    const cur = data.current;
    const unitSymbol = data.unit === 'imperial' ? '°F' : '°C';
    const speedUnit = data.unit === 'imperial' ? 'mph' : 'km/h';

    // City & Country
    document.getElementById('cityName').textContent = loc.name;
    document.getElementById('countryCode').textContent = loc.countryCode || loc.country || '';

    // Date & Time
    const now = new Date();
    const options = { weekday: 'long', hour: '2-digit', minute: '2-digit' };
    document.getElementById('currentDateTime').textContent = now.toLocaleDateString('en-US', options);

    // Weather Condition
    document.getElementById('conditionText').textContent = cur.condition;
    const conditionIcon = document.getElementById('conditionIcon');
    conditionIcon.className = `fa-solid ${cur.icon || 'fa-cloud-sun'} condition-icon`;

    // Temperature
    document.getElementById('currentTemp').textContent = Math.round(cur.temperature);
    document.getElementById('tempUnitDisplay').textContent = unitSymbol;
    document.getElementById('feelsLikeTemp').textContent = `${Math.round(cur.feelsLike)}${unitSymbol}`;

    // Vitals
    document.getElementById('humidityVal').textContent = `${cur.humidity}%`;
    document.getElementById('windSpeedVal').textContent = `${cur.windSpeed} ${speedUnit}`;
    document.getElementById('pressureVal').textContent = `${Math.round(cur.pressure)} hPa`;
    document.getElementById('uvVal').textContent = `${cur.uvIndex.toFixed(1)}`;

    // Render 24h Hourly Forecast
    renderHourly(data.hourlyForecast, unitSymbol);

    // Render 5-Day Forecast
    renderDailyForecast(data.dailyForecast, unitSymbol);

    // Dynamic Atmospheric Glow
    updateAmbientGlow(cur.weatherCode, cur.day);
}

function renderHourly(hourlyList, unitSymbol) {
    const container = document.getElementById('hourlyContainer');
    container.innerHTML = '';

    if (!hourlyList || hourlyList.length === 0) {
        container.innerHTML = '<p style="color: #64748b;">No hourly data available.</p>';
        return;
    }

    hourlyList.slice(0, 24).forEach(h => {
        const item = document.createElement('div');
        item.className = 'hourly-item';
        item.innerHTML = `
            <span class="hourly-time">${h.time}</span>
            <i class="fa-solid ${h.icon || 'fa-cloud'} hourly-icon"></i>
            <span class="hourly-temp">${Math.round(h.temperature)}${unitSymbol}</span>
            <span class="hourly-precip"><i class="fa-solid fa-droplet"></i> ${h.precipitationProbability}%</span>
        `;
        container.appendChild(item);
    });
}

function renderDailyForecast(dailyList, unitSymbol) {
    const container = document.getElementById('forecastContainer');
    container.innerHTML = '';

    if (!dailyList || dailyList.length === 0) {
        container.innerHTML = '<p style="color: #64748b;">No forecast data available.</p>';
        return;
    }

    dailyList.slice(0, 5).forEach(day => {
        const card = document.createElement('div');
        card.className = 'forecast-card';
        card.innerHTML = `
            <div class="forecast-day">${day.dayOfWeek}</div>
            <div class="forecast-date">${formatShortDate(day.date)}</div>
            <i class="fa-solid ${day.icon || 'fa-cloud-sun'} forecast-icon"></i>
            <div class="forecast-cond">${day.condition}</div>
            <div class="forecast-temps">
                <span class="temp-max">${Math.round(day.maxTemp)}${unitSymbol}</span>
                <span class="temp-min">${Math.round(day.minTemp)}${unitSymbol}</span>
            </div>
            <div class="forecast-rain">
                <i class="fa-solid fa-umbrella"></i> ${day.precipitationProbability}%
            </div>
        `;
        container.appendChild(card);
    });
}

function updateAmbientGlow(weatherCode, isDay) {
    const glow1 = document.querySelector('.glow-1');
    const glow2 = document.querySelector('.glow-2');

    if (!isDay) {
        // Night
        glow1.style.background = 'radial-gradient(circle, #312e81 0%, transparent 70%)';
        glow2.style.background = 'radial-gradient(circle, #1e1b4b 0%, transparent 70%)';
    } else if (weatherCode >= 51 && weatherCode <= 67) {
        // Rain
        glow1.style.background = 'radial-gradient(circle, #0284c7 0%, transparent 70%)';
        glow2.style.background = 'radial-gradient(circle, #0f766e 0%, transparent 70%)';
    } else if (weatherCode >= 71 && weatherCode <= 86) {
        // Snow
        glow1.style.background = 'radial-gradient(circle, #e0f2fe 0%, transparent 70%)';
        glow2.style.background = 'radial-gradient(circle, #93c5fd 0%, transparent 70%)';
    } else {
        // Sunny / Clear
        glow1.style.background = 'radial-gradient(circle, #f59e0b 0%, transparent 70%)';
        glow2.style.background = 'radial-gradient(circle, #38bdf8 0%, transparent 70%)';
    }
}

// Helpers
function formatShortDate(dateStr) {
    if (!dateStr) return '';
    const d = new Date(dateStr);
    return d.toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
}

function showLoading(isLoading) {
    const loader = document.getElementById('loadingIndicator');
    const dashboard = document.getElementById('weatherDashboard');
    if (isLoading) {
        loader.classList.remove('hidden');
        dashboard.style.opacity = '0.35';
    } else {
        loader.classList.add('hidden');
        dashboard.style.opacity = '1';
    }
}

function showToast(message) {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.classList.remove('hidden');
    setTimeout(() => {
        toast.classList.add('hidden');
    }, 3200);
}

function escapeHtml(text) {
    if (!text) return '';
    return text.replace(/[&<>"']/g, m => ({
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#39;'
    })[m]);
}

