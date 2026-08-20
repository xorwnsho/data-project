const STORAGE_KEY = "sangkwon-ai:favorites";

function read() {
	try {
		const raw = localStorage.getItem(STORAGE_KEY);
		return raw ? JSON.parse(raw) : [];
	} catch {
		return [];
	}
}

function write(list) {
	localStorage.setItem(STORAGE_KEY, JSON.stringify(list));
}

export function getFavorites() {
	return read();
}

export function isFavorite(region, industry) {
	return read().some((f) => f.region === region && (f.industry || "") === (industry || ""));
}

export function addFavorite(region, industry) {
	const list = read();
	if (list.some((f) => f.region === region && (f.industry || "") === (industry || ""))) {
		return list;
	}
	const next = [...list, { region, industry: industry || "", addedAt: Date.now() }];
	write(next);
	return next;
}

export function removeFavorite(region, industry) {
	const next = read().filter((f) => !(f.region === region && (f.industry || "") === (industry || "")));
	write(next);
	return next;
}
