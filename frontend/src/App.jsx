import { useState } from "react";
import "leaflet/dist/leaflet.css";
import QueryForm from "./components/QueryForm";
import ReportPanel from "./components/ReportPanel";
import StoreMap from "./components/StoreMap";
import { extractErrorMessage, postQuery } from "./api/queryApi";
import "./App.css";

export default function App() {
	const [loading, setLoading] = useState(false);
	const [error, setError] = useState(null);
	const [result, setResult] = useState(null);

	async function handleSubmit(message) {
		setLoading(true);
		setError(null);
		try {
			const data = await postQuery(message);
			setResult(data);
		} catch (err) {
			setError(extractErrorMessage(err));
			setResult(null);
		} finally {
			setLoading(false);
		}
	}

	return (
		<div className="app">
			<header className="app-header">
				<div className="app-badge">AI</div>
				<div>
					<h1>대전·세종 AI 입지분석 상담사</h1>
					<p>소상공인시장진흥공단 상가업소 데이터를 근거로, 자연어로 물어보면 AI가 상권을 해석해드립니다.</p>
				</div>
			</header>

			<main className="app-main">
				<QueryForm onSubmit={handleSubmit} loading={loading} />

				{error && <div className="error-box">{error}</div>}

				{result && (
					<div className="result-grid">
						<ReportPanel result={result} />
						<StoreMap stats={result.stats} />
					</div>
				)}
			</main>
		</div>
	);
}
