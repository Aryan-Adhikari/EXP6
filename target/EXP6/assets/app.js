document.addEventListener("submit", (event) => {
    const form = event.target;
    if (!(form instanceof HTMLFormElement) || !form.hasAttribute("data-prevent-double-submit")) {
        return;
    }

    if (form.dataset.submitting === "true") {
        event.preventDefault();
        return;
    }

    form.dataset.submitting = "true";
    for (const button of form.querySelectorAll('button[type="submit"], input[type="submit"]')) {
        button.disabled = true;
    }
});
