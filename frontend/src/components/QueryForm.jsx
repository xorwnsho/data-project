import { useState } from "react";

const EXAMPLES = [
	"둔산동에서 카페 하나 차리려는데 괜찮을까?",
	"노은동에 치킨집 내면 경쟁이 심할까?",
	"나성동 편의점 자리 어때?",
];

export default function QueryForm({ onSubmit, loading, initialMessage }) {
	const [message, setMessage] = useState(initialMessage ?? "");

	function handleSubmit(e) {
		e.preventDefault();
		if (!message.trim() || loading) return;
		onSubmit(message.trim());
	}

	return (
		<form className="query-form" onSubmit={handleSubmit}>
			<textarea
				className="query-input"
				placeholder="예: 둔산동에서 카페 하나 차리려는데 괜찮을까?"
				value={message}
				onChange={(e) => setMessage(e.target.value)}
				rows={3}
			/>
			<div className="query-examples">
				{EXAMPLES.map((example) => (
					<button
						type="button"
						key={example}
						className="example-chip"
						onClick={() => setMessage(example)}
					>
						{example}
					</button>
				))}
			</div>
			<div className="form-footer">
				<button className="submit-button" type="submit" disabled={loading || !message.trim()}>
					{loading ? "분석 중..." : "AI 상권 분석 받기"}
				</button>
			</div>
		</form>
	);
}
