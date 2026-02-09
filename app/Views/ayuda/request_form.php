<div class="row justify-content-center">
    <div class="col-lg-8 col-xl-7">
        <div class="card shadow-sm border-0">
            <div class="card-body p-4">
                <h1 class="h4 mb-3">Request Ayuda</h1>
                <p class="text-muted mb-4 small">
                    Fill out the form below to request assistance from the barangay.
                </p>

                <form>
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label for="request_type" class="form-label mb-1 small fw-semibold text-muted text-uppercase">
                                Request Type *
                            </label>
                            <select id="request_type" class="form-select">
                                <option selected disabled>Select type of assistance</option>
                                <option>Financial Assistance</option>
                                <option>Medical / Health</option>
                                <option>Food Packs / Relief Goods</option>
                                <option>Document Processing</option>
                                <option>Legal Advice</option>
                                <option>Other</option>
                            </select>
                        </div>
                        <div class="col-md-6">
                            <label for="urgency" class="form-label mb-1 small fw-semibold text-muted text-uppercase">
                                Urgency Level *
                            </label>
                            <select id="urgency" class="form-select">
                                <option>Low - Can wait</option>
                                <option selected>Medium - Needed soon</option>
                                <option>High - Urgent</option>
                            </select>
                        </div>
                        <div class="col-12">
                            <label for="description" class="form-label mb-1 small fw-semibold text-muted text-uppercase">
                                Description of Need *
                            </label>
                            <textarea
                                id="description"
                                class="form-control"
                                rows="4"
                                placeholder="Please describe your situation and what specific help you need..."
                            ></textarea>
                        </div>
                        <div class="col-md-6">
                            <label for="preferred_date" class="form-label mb-1 small fw-semibold text-muted text-uppercase">
                                Preferred Date
                            </label>
                            <input type="date" id="preferred_date" class="form-control">
                            <div class="form-text small">
                                Optional preference for when you can receive help.
                            </div>
                        </div>
                        <div class="col-md-6">
                            <label for="contact_number" class="form-label mb-1 small fw-semibold text-muted text-uppercase">
                                Contact Number *
                            </label>
                            <input
                                type="tel"
                                id="contact_number"
                                class="form-control"
                                placeholder="e.g. 09171234567"
                            >
                        </div>
                    </div>

                    <div class="alert alert-primary mt-4 mb-3 small">
                        <strong>Important Reminder:</strong>
                        <ul class="mb-0 ps-3 mt-1">
                            <li>All requests are subject to evaluation and approval.</li>
                            <li>You may be contacted for further interview or validation.</li>
                            <li>Providing false information may lead to disqualification.</li>
                        </ul>
                    </div>

                    <button type="button" class="btn btn-success w-100 fw-semibold">
                        Submit Request
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

