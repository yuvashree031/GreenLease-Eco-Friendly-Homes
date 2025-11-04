// GreenLease JavaScript Application
document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Eco score color coding
    function updateEcoScoreColors() {
        const ecoScores = document.querySelectorAll('.eco-score');
        ecoScores.forEach(function(scoreElement) {
            const score = parseFloat(scoreElement.textContent);
            if (score >= 8.0) {
                scoreElement.classList.add('eco-rating-excellent');
            } else if (score >= 6.0) {
                scoreElement.classList.add('eco-rating-good');
            } else if (score >= 4.0) {
                scoreElement.classList.add('eco-rating-fair');
            } else {
                scoreElement.classList.add('eco-rating-poor');
            }
        });
    }

    // Property card animations
    function animatePropertyCards() {
        const cards = document.querySelectorAll('.property-card');
        const observer = new IntersectionObserver(function(entries) {
            entries.forEach(function(entry) {
                if (entry.isIntersecting) {
                    entry.target.classList.add('fade-in-up');
                }
            });
        }, { threshold: 0.1 });

        cards.forEach(function(card) {
            observer.observe(card);
        });
    }

    // Search functionality
    function initializeSearch() {
        const cityInput = document.getElementById('city');
        if (cityInput) {
            // Add autocomplete functionality
            cityInput.addEventListener('input', function() {
                const query = this.value;
                if (query.length > 2) {
                    // Here you could implement AJAX city suggestions
                    // fetch('/api/search/cities?query=' + encodeURIComponent(query))
                    //     .then(response => response.json())
                    //     .then(cities => showCitySuggestions(cities));
                }
            });
        }
    }

    // Property filters
    function initializeFilters() {
        const filterForms = document.querySelectorAll('form[action*="/properties"]');
        filterForms.forEach(function(form) {
            const inputs = form.querySelectorAll('input, select');
            inputs.forEach(function(input) {
                if (input.type === 'range') {
                    input.addEventListener('input', function() {
                        const output = document.getElementById(this.id + '-output');
                        if (output) {
                            output.textContent = this.value;
                        }
                    });
                }
            });
        });
    }

    // Star rating display
    function displayStarRatings() {
        const ratings = document.querySelectorAll('.star-rating');
        ratings.forEach(function(rating) {
            const score = parseInt(rating.dataset.rating);
            const maxStars = parseInt(rating.dataset.max) || 5;
            let stars = '';
            
            for (let i = 1; i <= maxStars; i++) {
                if (i <= score) {
                    stars += '<i class="fas fa-star"></i>';
                } else {
                    stars += '<i class="far fa-star"></i>';
                }
            }
            rating.innerHTML = stars;
        });
    }

    // Eco rating display with leaf icons
    function displayEcoRatings() {
        const ecoRatings = document.querySelectorAll('.eco-star-rating');
        ecoRatings.forEach(function(rating) {
            const score = parseInt(rating.dataset.rating);
            const maxLeaves = parseInt(rating.dataset.max) || 5;
            let leaves = '';
            
            for (let i = 1; i <= maxLeaves; i++) {
                if (i <= score) {
                    leaves += '<i class="fas fa-leaf text-success"></i>';
                } else {
                    leaves += '<i class="far fa-leaf text-muted"></i>';
                }
            }
            rating.innerHTML = leaves;
        });
    }

    // Property image lazy loading
    function initializeLazyLoading() {
        const images = document.querySelectorAll('img[data-src]');
        const imageObserver = new IntersectionObserver(function(entries, observer) {
            entries.forEach(function(entry) {
                if (entry.isIntersecting) {
                    const img = entry.target;
                    img.src = img.dataset.src;
                    img.classList.remove('lazy');
                    imageObserver.unobserve(img);
                }
            });
        });

        images.forEach(function(img) {
            imageObserver.observe(img);
        });
    }

    // Form validation
    function initializeFormValidation() {
        const forms = document.querySelectorAll('.needs-validation');
        forms.forEach(function(form) {
            form.addEventListener('submit', function(event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            });
        });
    }

    // Feedback form enhancements
    function initializeFeedbackForm() {
        const feedbackForm = document.getElementById('feedbackForm');
        if (feedbackForm) {
            const ratingInputs = feedbackForm.querySelectorAll('input[type="range"]');
            ratingInputs.forEach(function(input) {
                const output = document.getElementById(input.id + '-value');
                if (output) {
                    input.addEventListener('input', function() {
                        output.textContent = this.value;
                        updateRatingDisplay(this);
                    });
                }
            });
        }
    }

    function updateRatingDisplay(input) {
        const container = input.closest('.rating-container');
        if (container) {
            const stars = container.querySelectorAll('.rating-star');
            const value = parseInt(input.value);
            
            stars.forEach(function(star, index) {
                if (index < value) {
                    star.classList.remove('far');
                    star.classList.add('fas');
                } else {
                    star.classList.remove('fas');
                    star.classList.add('far');
                }
            });
        }
    }

    // Success/Error message handling
    function handleMessages() {
        const urlParams = new URLSearchParams(window.location.search);
        
        if (urlParams.has('success')) {
            const successType = urlParams.get('success');
            showSuccessMessage(getSuccessMessage(successType));
        }
        
        if (urlParams.has('error')) {
            const errorType = urlParams.get('error');
            showErrorMessage(getErrorMessage(errorType));
        }
    }

    function getSuccessMessage(type) {
        const messages = {
            'added': 'Property added successfully!',
            'feedback': 'Thank you for your feedback!',
            'verified': 'Feedback verified successfully!',
            'deleted': 'Item deleted successfully!'
        };
        return messages[type] || 'Operation completed successfully!';
    }

    function getErrorMessage(type) {
        const messages = {
            'notfound': 'The requested item was not found.',
            'feedback': 'There was an error submitting your feedback.',
            'validation': 'Please check your input and try again.'
        };
        return messages[type] || 'An error occurred. Please try again.';
    }

    function showSuccessMessage(message) {
        showToast(message, 'success');
    }

    function showErrorMessage(message) {
        showToast(message, 'error');
    }

    function showToast(message, type) {
        const toastContainer = document.getElementById('toast-container') || createToastContainer();
        const toast = createToast(message, type);
        toastContainer.appendChild(toast);
        
        const bsToast = new bootstrap.Toast(toast);
        bsToast.show();
        
        toast.addEventListener('hidden.bs.toast', function() {
            toast.remove();
        });
    }

    function createToastContainer() {
        const container = document.createElement('div');
        container.id = 'toast-container';
        container.className = 'toast-container position-fixed top-0 end-0 p-3';
        container.style.zIndex = '1055';
        document.body.appendChild(container);
        return container;
    }

    function createToast(message, type) {
        const toastEl = document.createElement('div');
        toastEl.className = `toast align-items-center text-white bg-${type === 'success' ? 'success' : 'danger'} border-0`;
        toastEl.setAttribute('role', 'alert');
        toastEl.innerHTML = `
            <div class="d-flex">
                <div class="toast-body">
                    <i class="fas fa-${type === 'success' ? 'check-circle' : 'exclamation-circle'} me-2"></i>
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        `;
        return toastEl;
    }

    // Initialize all components
    updateEcoScoreColors();
    animatePropertyCards();
    initializeSearch();
    initializeFilters();
    displayStarRatings();
    displayEcoRatings();
    initializeLazyLoading();
    initializeFormValidation();
    initializeFeedbackForm();
    handleMessages();

    // Smooth scrolling for anchor links
    document.querySelectorAll('a[href^="#"]').forEach(function(anchor) {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({ behavior: 'smooth' });
            }
        });
    });

    // Back to top button
    const backToTopButton = document.createElement('button');
    backToTopButton.innerHTML = '<i class="fas fa-arrow-up"></i>';
    backToTopButton.className = 'btn btn-success position-fixed bottom-0 end-0 m-3 rounded-circle';
    backToTopButton.style.display = 'none';
    backToTopButton.style.zIndex = '1050';
    backToTopButton.addEventListener('click', function() {
        window.scrollTo({ top: 0, behavior: 'smooth' });
    });
    document.body.appendChild(backToTopButton);

    window.addEventListener('scroll', function() {
        if (window.pageYOffset > 300) {
            backToTopButton.style.display = 'block';
        } else {
            backToTopButton.style.display = 'none';
        }
    });
});

// Global utility functions
window.GreenLease = {
    formatRent: function(amount) {
        return new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD',
            minimumFractionDigits: 0
        }).format(amount);
    },

    formatEcoScore: function(score) {
        return parseFloat(score).toFixed(1);
    },

    getEcoRatingText: function(score) {
        if (score >= 8.0) return 'Excellent';
        if (score >= 6.0) return 'Good';
        if (score >= 4.0) return 'Fair';
        return 'Poor';
    },

    getEcoRatingClass: function(score) {
        if (score >= 8.0) return 'eco-rating-excellent';
        if (score >= 6.0) return 'eco-rating-good';
        if (score >= 4.0) return 'eco-rating-fair';
        return 'eco-rating-poor';
    }
};