import axios from "axios";

const client = axios.create({
	baseURL: import.meta.env.VITE_API_BASE_URL,
});

export async function postQuery(message) {
	const { data } = await client.post("/api/query", { message });
	return data;
}

export async function getRegions() {
	const { data } = await client.get("/api/regions");
	return data;
}

export async function getIndustries() {
	const { data } = await client.get("/api/industries");
	return data;
}

export async function getExplore(region, industry, radius = 500) {
	const { data } = await client.get("/api/explore", {
		params: { region, industry: industry || undefined, radius },
	});
	return data;
}

export async function postCompare(a, b) {
	const { data } = await client.post("/api/compare", { a, b });
	return data;
}

export function extractErrorMessage(error) {
	return error?.response?.data?.message ?? "요청 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해주세요.";
}
